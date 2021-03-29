# -*- coding: utf-8 -*-
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

# 소켓을 사용하기 위해서는 socket을 import해야 한다.
import socket, threading;

from tqdm.notebook import tqdm

import sys
if sys.version_info <= (2,7):
    reload(sys)
    sys.setdefaultencoding('utf-8')
import konlpy
from konlpy.tag import Kkma, Okt, Hannanum

import io
sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding = 'utf-8')
sys.stderr = io.TextIOWrapper(sys.stderr.detach(), encoding = 'utf-8')

global t_cnt
global w_cnt

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
            logger.info(i)
            logger.info(similar_word)
        except:
            continue
        for j in similar_word:

            if len(j[0]) == 1:
                continue
            else:
                similar_word_list.append(j[0])

    logger.info('similar_word_list')
    logger.info(similar_word_list)
    return similar_word_list

def getresset(similar_word_set, category, curs):
    logger.info('similar_word_set')
    logger.info(similar_word_set)
    res_list=[]
    for i in similar_word_set:
        sql = 'select pagename,trim(title), achieve, goal, id from test.crawl where category="%s" and title like "%%%s%%" and achieve>=90;'%(category,i)
        curs.execute(sql)
        pagename = curs.fetchall()

        length = len(pagename)
        if length > 3:
            length = 3

        for k in range(0,length):
            title = pagename[k][1].replace(u'\xa0', u' ') 
            logger.info('tite type : ')
            logger.info(type(title))
            logger.info('pagename[k][1] type : ')
            logger.info(type(pagename[k][1]))
            
            res_list.append((pagename[k][0], title, pagename[k][2], pagename[k][3], pagename[k][4]))   # pagename, title, achieve, goal
            # logger.info('res_list')
            # logger.info(res_list)
    res_set = set(res_list)
    logger.info('res_set')
    logger.info(res_set)

    return res_set

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
        pred.append(k[4])

        if pred not in pred_list :
            pred_list.append(pred)
        conn.commit()
    return pred_list

def getcnt(res_set):
    w_cnt = 0
    t_cnt = 0

    for k in res_set:
        if k[0] == 'tumblbug': t_cnt+=1
        elif k[0] == 'wadiz': w_cnt+=1
        else: continue

def creatortitle(category, title):
    logger.info("before connect")
    conn = pymysql.connect(host='127.0.0.1', user='root', password='wdta2181',db='test', charset='utf8')
    curs = conn.cursor()
    logger.info('title')
    logger.info(title)
    logger.info('category')
    logger.info(category)
    #title = request.form['a']
    #category = request.form['ca']
    tokenized_project_title = set(okt.nouns(title))
    logger.info('tokenized_project_title')
    logger.info(tokenized_project_title)
    logger.info(type(tfidf_dict_10))
    
    rake = ['뱃지', '가방', '환경', '주간', '와디', '집사', '기념', '패딩', '효율', '웜뱃', '항균', '바디', '이상', '가성', '노하우', '캘린더', '한정', '자석', '스티커', '리버', '성적', '식물성', '섹스', '문구', '실용', '기름', '특허성', '주인공', '세트', '공식', '동시', '패드', '우드', '양말', '모이', '품질', '사람', '니트', '눈덧신토끼', '위시', '과학', '생존', '걱정', '진리', '품격', '한국', '육회', '신경과학', '스타', '공기', '리듬게임', '앵콜', '고유', '유지', '리버시', '기록', '탐방', '해결', '데스크', '입지', '어머니', '방향', '해치', '마일', '작가', '포스터', '레이', '삼치', '손글씨', '노트', '서랍', '순간', '가격', '퀄리티', '바지', '인터뷰', '싱어송라이터', '레밋', '특화', '곳도', '세상', '크리스마스', '강아지', '인투', '나이트', '자기', '스칼렛', '사용', '옆집', '하루', '생각', '온전', '바오', '소재', '연구', '뮤지컬', '대학교', '방식', '표현', '실패', '펜더', '영화제', '여친', '구성', '아가', '달력', '페이지스', '빅픽처', '맞춤법', '카드', '티셔츠', '애플', '차량', '비서실', '스포츠', '폼클렌저', '비키', '개운', '누리', '기억', '가시', '스타일', '집중', '놀이', '교육', '마술', '선물', '피어', '홈텐더', '버스', '음반', '일상', '캐릭터', '건강', '유네스코', '재기', '혼자', '기차', '인류', '단편영화', '모던', '포트폴리오', '만들기', '향기', '방석', '바다', '블렌더', '공간', '무드', '원화', '이해', '플레이', '익명', '청춘', '붙이', '기분', '빈티', '애용', '케이스', '느낌', '기능', '리아', '키트', '고향', '케냐', '천상병', '프로젝트', '갈피', '커플링', '미술관', '소리', '시작', '레몬', '김치', '이름', '기기', '우울', '드라이브', '활용', '레이아웃', '숲속', '에세이', '여행', '여자', '시절', '경험', '냄새', '머리', '호박', '앰플', '패션', '중요성', '가장', '함유량', '플라워', '수기', '아리차', '사랑', '이태리', '설계', '아이', '무스', '여정', '공구', '그때', '육아', '브랜드', '입다', '유해', '발효', '오프', '정규', '공존', '고양이', '네이버웹툰', '구도', '실내', '휴대', '서체', '시대', '렌즈', '헤럴드', '살균제', '만족', '전시회', '서나', '바람막이', '그림', '전골', '사이즈', '퍼포먼스', '양털', '공자', '향수', '누적', '여기', '워시', '자수', '천연', '자리', '컬러', '마음', '체험', '하늬', '성폭력', '지기', '핸드', '탈취', '완성', '레깅스', '시리즈', '재탄생', '도마', '토트', '탈모', '바보', '컬렉션', '퍼스트', '한눈', '파우치', '마스크', '여성', '하우스', '유리컵', '양념', '쥬얼리', '논리', '월렛', '깔창', '필수', '워커', '설악산', '소설', '생활', '요리', '요리사', '효소', '용어', '권력', '가을', '성주목', '슬랙스핏', '반지', '책가방', '안감', '쿠셔닝', '캠핑', '괴담', '추리', '중독', '일기', '끝내', '부담', '숨숨', '사과', '오메가', '전하', '카혼', '크림', '오빠', '알다', '피크', '점퍼', '운동복', '무대', '시계', '국민', '이니셜', '단백질', '아노락', '원조', '찜질', '쓰기', '나주', '와플', '지갑', '전자', '공개', '홀로', '에디', '쿠션', '흰둥이', '컴퓨터', '자켓', '뮬러', '강수', '피부', '겨울', '코듀', '커피', '요가', '리운스', '눈앞', '아치', '생산', '유통', '경량', '콜드', '상업', '롤러코스터', '블락', '대로', '고기', '서포터', '제리', '한국어', '다이아몬드', '초콜릿', '반짝', '포투', '이불', '두루', '스마트', '수건', '마케팅', '스케치업', '반려', '라이트백', '조화', '방어', '제주', '가향', '브이', '벤데타', '히트', '캐리어', '편안함', '국내', '펀딩', '드라이', '버터', '오픈', '리얼', '드루', '특허', '불꽃심장', '회사', '감성', '후기', '영감', '수분', '수분크림', '월경', '여름방학', '퍼스', '선택', '보습', '테크', '취미', '무게', '모집', '스웨터', '행운', '페스', '하자', '이야기', '용기', '카키', '어워드', '퍼센트', '매일', '실현', '다리', '기술', '프로', '닭고기', '쫄깃', '별로', '다운', '확인', '엑스트라', '한양', '제작', '체크', '웹툰', '작업', '방법', '담요', '세계', '장착', '장인', '폴딩', '매트', '미니미', '나은', '지식', '올해', '케이블', '어묵', '화장', '만족도', '투잡', '남자', '귀환', '코팅', '하주', '무릎', '프린터기', '누구', '살균', '테크니컬', '포즈', '트래블러', '명기', '주택가', '입양', '쭉쭉', '채굴', '정장', '치킨', '서명', '정리', '괴물', '개발', '실화', '장벽', '아르기닌', '히알루론산', '프리미엄', '다비드', '응원', '무량']
    #logger.info(rake)

    tfidf_dict = tfidf_dict_10 + rake
    tfidf_dict_set = set(tfidf_dict)
    selected_word = []
    for i in tokenized_project_title:
        if i in tfidf_dict_set:
            selected_word.append(i)
            continue
    similar_word_list = []
    similar_word_list = getsimilarwordlist(selected_word, similar_word_list)

    similar_word_list = similar_word_list + selected_word

    similar_word_set = set(similar_word_list)
    res_set = set()
    res_set = getresset(similar_word_set, category, curs)

    getcnt(res_set)

    pred = getCreatorPredList(res_set, curs, conn)

    logger.info(pred)
    logger.info(type(pred))
    conn.close()
    return pred

def creatorkeyword(category, keyword):
    conn = pymysql.connect(host='127.0.0.1', user='root', password='wdta2181',db='test', charset='utf8')
    curs = conn.cursor()

    selected_word = keyword.split(' ')

    similar_word_list = []
    similar_word_list = getsimilarwordlist(selected_word,similar_word_list)

    if len(selected_word) < 5:
        similar_word_list = similar_word_list + selected_word

    similar_word_set = set(similar_word_list)


    res_set= set()
    res_set = getresset(similar_word_set, category,curs)

    getcnt(res_set)

    pred = getCreatorPredList(res_set, curs, conn)

    logger.info(pred)
    logger.info(type(pred))
    conn.close()

    return pred

def binder(client_socket, addr):
# 커넥션이 되면 접속 주소가 나온다.
    print('Connected by', addr);

    try:
# 접속 상태에서는 클라이언트로 부터 받을 데이터를 무한 대기한다.
# 만약 접속이 끊기게 된다면 except가 발생해서 접속이 끊기게 된다.
        while True:
# socket의 recv함수는 연결된 소켓으로부터 데이터를 받을 대기하는 함수입니다. 최초 4바이트를 대기합니다.
            data = client_socket.recv(4);

# 최초 4바이트는 전송할 데이터의 크기이다. 그 크기는 little big 엔디언으로 byte에서 int형식으로 변환한다.
# C#의 BitConverter는 big엔디언으로 처리된다.
            length = int.from_bytes(data, "little");
# 다시 데이터를 수신한다.
            data = client_socket.recv(length);
            logger.info("data")
            logger.info(data)
            #logger.info("after receiving, data : ", data)
            #logger.info(data)
# 수신된 데이터를 str형식으로 decode한다.
            msg = data.decode();
            logger.info("after decoding")
            logger.info("data")
            logger.info(data)
            logger.info("msg")
            logger.info(msg)

# 수신된 메시지를 콘솔에 출력한다.
            #logger.info('Received from ', msg);
# 수신된 메시지 앞에 「echo:」 라는 메시지를 붙힌다.
            #msg = "echo : " + msg;
            if msg[0:4] == "user":
                logger.info(msg)
                pred_list = []
                pred_list = supporterpage(msg[4:])

                logger.info(pred_list)
                msg = ''
                for i in pred_list:
                    msg = msg+''.join(str(i))

            elif "title" in msg :
                logger.info('cate안')
                logger.info('msg')
                logger.info(msg)
                msgList = list()
                msgList = msg.split('title')
                logger.info(msgList)
                pred_list = []
                pred_list = creatortitle(msgList[0], msgList[1])

                logger.info(pred_list)
                """
                msg = ''
                for i in pred_list:
                    msg = msg+''.join(str(i))
                """
            elif "keyword" in msg :
                logger.info('keyword안')
                logger.info('msg')
                logger.info(msg)
                msgList = list()
                msgList = msg.split('keyword')
                logger.info(msgList)
                pred_list = []
                pred_list = creatorkeyword(msgList[0], msgList[1])

                logger.info(pred_list)

            pred_list = str(pred_list)
            logger.info('pred_list')
            logger.info(pred_list)
            # logger.info(msg)
            #elif msg[0:4] == ""
            # 바이너리(byte)형식으로 변환한다.
            data = pred_list.encode();
            # 바이너리의 데이터 사이즈를 구한다.
            length = len(data);
            # 데이터 사이즈를 little 엔디언 형식으로 byte로 변환한 다음 전송한다.
            client_socket.sendall(length.to_bytes(4, byteorder='little'));
            # 데이터를 클라이언트로 전송한다.
            client_socket.sendall(data);

    except:
# 접속이 끊기면 except가 발생한다.
        print("except : " , addr);
    #finally:
# 접속이 끊기면 socket 리소스를 닫는다.
        #client_socket.close();

def supporterpage(username):
    conn = pymysql.connect(host='127.0.0.1', user='root', password='wdta2181',db='test', charset='utf8')
    curs = conn.cursor()

    logger.info("supporterpage 안")

    sql = "select distinct trim(I.title) from test.picklist I, test.crawl C where email = '%s' and C.title = I.title;"%username
    curs.execute(sql)
    sent = curs.fetchall()
    logger.info("sent")
    logger.info(sent)
    #curs.execute(sql)
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
        sql3 = "select distinct I.title, C.category from test.picklist I, test.crawl C where email like '{username}' and C.title = I.title and I.title like '%%{word}%%';".format(username=username, word=i);
        curs.execute(sql3)
        rows = curs.fetchall()

        for j in rows:
            if i in j[0]:
                final_word_list.append(i)
                category_list.append(j[1])

    word_len = len(final_word_list)
    category_len = len(category_list)

    final_projects=[]


    for i in tqdm(range(word_len)):
        similar_word=model29.wv.most_similar(positive=[final_word_list[i]],topn=5)

        for j in similar_word:
            if len(j[0])==1:
                continue
            else:
                print("j값비교 : ")
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
            pred.append(k[3])
            if pred not in pred_list :
                pred_list.append(pred)

    project_num = len(pred_list)


    return pred_list

import logging

def __get_logger():
    """로거 인스턴스 반환
    """

    __logger = logging.getLogger('logger')

    # 로그 포멧 정의
    formatter = logging.Formatter(
        'BATCH##AWSBATCH##%(levelname)s##%(asctime)s##%(message)s >> @@file::%(filename)s@@line::%(lineno)s')
    # 스트림 핸들러 정의
    stream_handler = logging.StreamHandler()
    # 각 핸들러에 포멧 지정
    stream_handler.setFormatter(formatter)
    # 로거 인스턴스에 핸들러 삽입
    __logger.addHandler(stream_handler)
    # 로그 레벨 정의
    __logger.setLevel(logging.DEBUG)

    return __logger


if __name__ == "__main__":
    print("start")
    logger = __get_logger()
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM);
    # 소켓 레벨과 데이터 형태를 설정한다.
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1);
    # 서버는 복수 ip를 사용하는 pc의 경우는 ip를 지정하고 그렇지 않으면 None이 아닌 ''로 설정한다.
    # 포트는 pc내에서 비어있는 포트를 사용한다. cmd에서 netstat -an | find "LISTEN"으로 확인할 수 있다.
    server_socket.bind(('', 9000));
    # server 설정이 완료되면 listen를 시작한다.
    server_socket.listen();

    try:
        # 서버는 여러 클라이언트를 상대하기 때문에 무한 루프를 사용한다.
        while True:
            # client로 접속이 발생하면 accept가 발생한다.
            # 그럼 client 소켓과 addr(주소)를 튜플로 받는다.
            client_socket, addr = server_socket.accept();
            th = threading.Thread(target=binder, args = (client_socket,addr));
            # 쓰레드를 이용해서 client 접속 대기를 만들고 다시 accept로 넘어가서 다른 client를 대기한다.
            th.start();
    except:
        print("server");
    finally:
            # 에러가 발생하면 서버 소켓을 닫는다.
        server_socket.close();