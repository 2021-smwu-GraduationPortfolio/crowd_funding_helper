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

import sys
if sys.version_info <= (2,7):
    reload(sys)
    sys.setdefaultencoding('utf-8')
import konlpy
from konlpy.tag import Kkma, Okt, Hannanum

global t_cnt
global w_cnt

model = Word2Vec.load('NaverMovie30.model')

app = Flask(__name__)

article_data = pd.read_csv('mixver10.txt', encoding='utf-8', header= None)
documents = [' '.join(i[0].split(' ')[1:]) for i in article_data.values]

as_one = ''
for document in documents:
    as_one = as_one + ' ' + document
words = as_one.split()

counts = Counter(words)

vocab = sorted(counts, key=counts.get, reverse=True)

word2idx = {word.encode("utf8").decode("utf8"): ii for ii, word in enumerate(vocab,1)}

idx2word = {ii: word for ii, word in enumerate(vocab)}

V = len(word2idx)
N = len(documents)

tf = CountVectorizer()

tf.fit_transform(documents)

tf.fit_transform(documents)[0:1].toarray()

tfidf = TfidfVectorizer(max_features = 1800, max_df=1, min_df=0)

#generate tf-idf term-document matrix
A_tfidf_sp = tfidf.fit_transform(documents)  #size D x V

tfidf_dict = tfidf.get_feature_names()

okt = Okt()
print('konlpy version = %s' % konlpy.__version__)

def getsimilarwordlist(selected_word, similar_word_list):

    for i in selected_word:
        try:
            similar_word=model.wv.most_similar(positive=[i],topn=2)
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
        sql = 'select pagename,trim(title), achieve, goal from test.crawl where category="%s" and title like "%%%s%%" and achieve>=90;'%(category,i)
        curs.execute(sql)
        pagename = curs.fetchall()

        length = len(pagename)
        if length > 3:
            length = 3

        for k in range(0,length):
            res_list.append((pagename[k][0], pagename[k][1], pagename[k][2], pagename[k][3]))   # pagename, title, achieve, goal

    res_set = set(res_list)
    return res_set

def getcnt(res_set):
    w_cnt = 0
    t_cnt = 0

    for k in res_set:
        if k[0] == 'tumblbug': t_cnt+=1
        elif k[0] == 'wadiz': w_cnt+=1
        else: continue

@app.route('/')
def man():
    return render_template('home.html')

@app.route('/creatortitle', methods=['POST'])
def creatortitle():
    conn = pymysql.connect(host='127.0.0.1', port=3307, user='root', password='Pami1227!*',db='test', charset='utf8')
    curs = conn.cursor()

    title = request.form['a']
    category = request.form['ca']
    tokenized_project_title = set(okt.nouns(title))

    selected_word = []
    for i in tokenized_project_title:
        if i in tfidf_dict:
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

    conn.close()

    pred = list()
    for k in res_set:
        pred.append(k[1])

    return render_template('after.html', data=pred)

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

    res_set= set()
    res_set = getresset(similar_word_set, category,curs)

    getcnt(res_set)

    conn.close()

    pred = list()
    for k in res_set:
        pred.append(k[1])

    return render_template('after.html', data=pred)


if __name__ == "__main__":
    app.run(port = 5000, debug=True)
