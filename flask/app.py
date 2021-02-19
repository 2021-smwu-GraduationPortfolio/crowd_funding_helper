from flask import Flask, render_template, request, redirect, url_for, jsonify
from gensim.models import Word2Vec
import pickle
import numpy as np
import pandas as pd
import matplotlib.font_manager as fm
import matplotlib.pyplot as plt
from future.utils import iteritems
from collections import Counter
from sklearn.manifold import TSNE
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.feature_extraction.text import CountVectorizer
import pymysql
from tqdm.notebook import tqdm
from flask import g

import sys
if sys.version_info <= (2,7):
    reload(sys)
    sys.setdefaultencoding('utf-8')
import konlpy
from konlpy.tag import Kkma, Okt, Hannanum
'''
global t_cnt
global w_cnt
'''
model29 = Word2Vec.load('NaverMovie29.model')
model30 = Word2Vec.load('NaverMovie30.model')

app = Flask(__name__)

article_data_10 = pd.read_csv('mixver10.txt', encoding='utf-8', header= None)
documents_10 = [' '.join(i[0].split(' ')[1:]) for i in article_data_10.values]

article_data_9 = pd.read_csv('mixver9.txt', encoding='utf-8', header= None)
documents_9 = [' '.join(i[0].split(' ')[1:]) for i in article_data_9.values]

as_one_10 = ''
for document in documents_10:
    as_one_10 = as_one_10 + ' ' + document
words_10 = as_one_10.split()
counts_10 = Counter(words_10)
vocab_10 = sorted(counts_10, key=counts_10.get, reverse=True)
word2idx_10 = {word.encode("utf8").decode("utf8"): ii for ii, word in enumerate(vocab_10,1)}
idx2word_10 = {ii: word for ii, word in enumerate(vocab_10)}
V = len(word2idx_10)
N = len(documents_10)
tf_10 = CountVectorizer()
tf_10.fit_transform(documents_10)
tf_10.fit_transform(documents_10)[0:1].toarray()
tfidf_10 = TfidfVectorizer(max_features = 1800, max_df=1, min_df=0)
#generate tf-idf term-document matrix
A_tfidf_sp_10 = tfidf_10.fit_transform(documents_10)  #size D x V
tfidf_dict_10 = tfidf_10.get_feature_names()

as_one_9 = ''
for document in documents_9:
    as_one_9 = as_one_9 + ' ' + document
words_9 = as_one_9.split()
counts_9 = Counter(words_9)
vocab_9 = sorted(counts_9, key=counts_9.get, reverse=True)
word2idx_9 = {word.encode("utf8").decode("utf8"): ii for ii, word in enumerate(vocab_9,1)}
idx2word_9 = {ii: word for ii, word in enumerate(vocab_9)}
V = len(word2idx_9)
N = len(documents_9)
tf_9 = CountVectorizer()
tf_9.fit_transform(documents_9)
tf_9.fit_transform(documents_9)[0:1].toarray()
tfidf_9 = TfidfVectorizer(max_features = 1000, max_df=1, min_df=0)
#generate tf-idf term-document matrix
A_tfidf_sp_9 = tfidf_9.fit_transform(documents_9)  #size D x V
tfidf_dict_9 = tfidf_9.get_feature_names()


okt = Okt()
print('konlpy version = %s' % konlpy.__version__)

def getsimilarwordlist(selected_word, similar_word_list):

    for i in selected_word:
        try:
            similar_word=model30.wv.most_similar(positive=[i],topn=2)
        except:
            continue
        for j in similar_word:

            if len(j[0]) == 1:
                continue
            else:
                similar_word_list.append(j[0])
    return similar_word_list

def getresset(similar_word_set, category, curs):
    res_list=[]
    for i in similar_word_set:
        sql = 'select pagename, trim(title), achieve, goal, id from test.crawl where category="%s" and title like "%%%s%%" and achieve>=90;'%(category,i)
        curs.execute(sql)
        pagename = curs.fetchall()

        length = len(pagename)
        #if length > 3:
        #    length = 3

        for k in range(0,length):
            res_list.append((pagename[k][0], pagename[k][1], pagename[k][2], pagename[k][3], pagename[k][4]))   # pagename, title, achieve, goal

    res_set = set(res_list)
    return res_set

def getcnt(res_set):
    g.w_cnt = 0
    g.t_cnt = 0

    for k in res_set:
        if k[0] == 'tumblbug': g.t_cnt+=1
        elif k[0] == 'wadiz': g.w_cnt+=1
        else: continue

def getCreatorPredList(res_set, curs, conn) :
    pred_list = list()
    for k in res_set:
        sql_url = "select url from test.urllist where id like '%d'" %k[4]
        curs.execute(sql_url)
        urll = curs.fetchall()
        pred = list()
        if urll :
            urll= urll[0][0]

        pred.append(k[0])
        pred.append(k[1])
        pred.append(urll)
        pred.append(k[2])
        pred.append(k[3])

        if pred not in pred_list :
            pred_list.append(pred)
        conn.commit()
    return pred_list


@app.route('/')
def main():
    return render_template('main.html')

@app.route('/creatorpage')
def selectpage():
    return render_template('home.html')

@app.route('/supporter')
def supporterinput():
    return render_template('supporterinput.html')

@app.route('/creatortitle', methods=['POST'])
def creatortitle():
    conn = pymysql.connect(host='127.0.0.1', port=3307, user='root', password='Pami1227!*',db='test', charset='utf8')
    curs = conn.cursor()

    title = request.form['a']
    category = request.form['ca']
    tokenized_project_title = set(okt.nouns(title))

    selected_word = []
    for i in tokenized_project_title:
        if i in tfidf_dict_10:
            selected_word.append(i)
            continue
    similar_word_list = []
    similar_word_list = getsimilarwordlist(selected_word, similar_word_list)

    if len(selected_word) < 5:
        similar_word_list = similar_word_list + selected_word

    similar_word_set = set(similar_word_list)
    res_set = set()
    res_set = getresset(similar_word_set, category, curs)

    getcnt(res_set)

    pred_list = getCreatorPredList(res_set, curs, conn)
    project_num = len(pred_list)

    conn.close()
    return render_template('after_creator.html', data=pred_list, project_num = project_num, w_cnt = g.w_cnt, t_cnt = g.t_cnt)


@app.route('/creatorkeyword', methods=['POST'])
def creatorkeyword():
    conn = pymysql.connect(host='127.0.0.1', port=3307, user='root', password='Pami1227!*',db='test', charset='utf8')
    curs = conn.cursor()

    category = request.form['ca']
    keyword = request.form['a']
    selected_word = keyword.split(' ')

    similar_word_list = []
    similar_word_list = getsimilarwordlist(selected_word,similar_word_list)
    similar_word_set = set(similar_word_list)

    print(1)
    print(similar_word_set)

    res_set= set()
    res_set = getresset(similar_word_set, category, curs)
    print(2)
    print(res_set)

    getcnt(res_set)

    pred_list = getCreatorPredList(res_set, curs, conn)
    project_num = len(pred_list)

    conn.close()
    return render_template('after_creator.html', data=pred_list, project_num = project_num, w_cnt = g.w_cnt, t_cnt = g.t_cnt)

@app.route('/supporterpage', methods=['POST'])
def supporterpage():
    conn = pymysql.connect(host='127.0.0.1', port=3307, user='root', password='Pami1227!*',db='test', charset='utf8')
    curs = conn.cursor()

    username = request.form['a']
    sql = "select distinct trim(I.title) from test.user_info I, test.crawl C where username = '%s' and C.title = I.title;"%username

    curs.execute(sql)
    sent = curs.fetchall()

    curs.execute(sql)
    title_with_category = curs.fetchall()

    noun_set = set(okt.nouns(str(sent)))
    selected_word = []

    sent_list = list(sent)
    sent_list2 = []
    for i in sent_list:
        sent_list2.append(list(i))

#selected_word : tokenized word from user's funding title list
    for i in noun_set:
        if i in tfidf_dict_9:
            selected_word.append(i)
            continue

    final_word_list = []
    category_list=[]

#final_word_list : selected words
    for i in selected_word:
        sql3 = "select distinct I.title, C.category from test.user_info I, test.crawl C where username like '{username}' and C.title = I.title and I.title like '%%{word}%%';".format(username=username, word=i);
        curs.execute(sql3)
        rows = curs.fetchall()

        for j in rows:
            if i in j[0]:
                final_word_list.append(i)
                category_list.append(j[1])

    word_len = len(final_word_list)
    category_len = len(category_list)

    final_projects=[]
    similar_word_list=[]

    for i in tqdm(range(word_len)):
        similar_word=model29.wv.most_similar(positive=[final_word_list[i]],topn=5)
        for j in similar_word:
            print('similar_word : ',similar_word)
            if len(j[0])==1:
                continue
            else:
                print(j[0])
                sql = "select DISTINCT pagename, category, trim(title), id from test.crawl where title like '%%%s%%'" %j[0]
                curs.execute(sql)
                words = curs.fetchall()
                conn.commit()
                length = len(words)
                #if length>3:
                #    length = 3
                for k in range(0,length):
                    tmp=[]
                    tmp.append(''.join(list(words[k][2])))
                    if words[k][1] == category_list[i] and tmp not in sent_list2 :
                        final_projects.append(words[k])


        res_set = set(final_projects)
        pred_list=list()

        for k in res_set :
            pred = list()
            sql_url = "select url from test.urllist where id = '%d'" %k[3]
            curs.execute(sql_url)
            urll = curs.fetchall()
            if urll:
                urll = urll[0][0]

            conn.commit()
            pred.append(k[1])
            pred.append(k[2])
            pred.append(urll)
            if pred not in pred_list :
                pred_list.append(pred)

    project_num = len(pred_list)
    print(pred_list)

    return render_template('after.html', data=pred_list, project_num = project_num)
if __name__ == "__main__":
    app.run(port = 5000, debug=True)
