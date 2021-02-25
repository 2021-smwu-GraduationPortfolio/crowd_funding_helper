import pandas as pd
import matplotlib.font_manager as fm
import matplotlib.pyplot as plt
from future.utils import iteritems
from collections import Counter
from sklearn.manifold import TSNE
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.feature_extraction.text import CountVectorizer
import pymysql
from gensim.models import word2vec
from tqdm.notebook import tqdm
import csv
import sys
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize

if sys.version_info <= (2,7):
    reload(sys)
    sys.setdefaultencoding('utf-8')
import konlpy
from konlpy.tag import Okt


conn = pymysql.connect(host='127.0.0.1', port=3306, user='root', password='0000',db='test', charset='utf8')
curs = conn.cursor()

sql = 'select trim(title) from test.crawl'
curs.execute(sql)
rows = curs.fetchall()

titlelist=[]

for row in rows:
    titlelist.append(row[0])

#print(titlelist)
#print(okt.nouns(str(titlelist)))

okt = Okt()
data = okt.nouns(str(titlelist))


maxparse = sys.maxsize
while True:
    try:
        csv.field_size_limit(maxparse)
        break
    except OverflowError:
        maxparse = int(maxparse/10)

 #네이버 영화 코퍼스를 읽는다.
rdw = str(titlelist)

 #트위터 형태소 분석기를 로드한다. Twiter가 KoNLPy v0.4.5 부터 Okt로 변경 되었다.
twitter = Okt()

 #텍스트를 한줄씩 처리합니다.
result = []
malist = twitter.pos(rdw, norm=True, stem=True)
r = []
for word in malist:
    #Josa”, “Eomi”, “'Punctuation” 는 제외하고 처리
    if not word[1] in ["Josa","Eomi","Punctuation"]:
        r.append(word[0])
     #형태소 사이에 공백 " "  을 넣습니다. 그리고 양쪽 공백을 지웁니다.
rl = (" ".join(r)).strip()
result.append(rl)

result = okt.nouns(str(result))
data = data + result

#print("data원본")
#print(data)
#print()

for i in range(0, len(data)-1):
    if data[i] == '맨' and data[i+1] == '투맨': data[i] = '맨투맨'
    if data[i] != '맨' and data[i+1] == '투맨': data[i+1] = '맨투맨'
    if data[i] == '패' and data[i+1] == '브릭': data[i] = '패브릭'
    if data[i] == '한' and data[i+1] == '돈': data[i] = '한돈'
    if data[i] == '클리' and data[i+1] == '닉': data[i] = '클리닉'
    if data[i] == '데' and data[i+1] == '일리': data[i] = '데일리'
    if data[i] == '콜드' and data[i+1] == '브루': data[i] = '콜드브루'
    if data[i] == '프로' and data[i+1] == '틴': data[i] = '프로틴'
    if data[i] == '코듀' and data[i+1] == '로이': data[i] = '코듀로이'
    if data[i] == '스웨' and data[i+1] == '트': data[i] = '스웨트'
    if data[i] == '팬' and data[i+1] == '츠': data[i] = '팬츠'
    if data[i] == '마' and data[i+1] == '그네' and data[i+2] == '틱': data[i] = '마그네틱'
    if data[i] == '논' and data[i+1] == '슬립': data[i] = '논슬립'
    if data[i] == '사' and data[i+1] == '첼': data[i] = '사첼'
    if data[i] == '국내' and data[i+1] == '산': data[i] = '국내산'
    if data[i] == '가성' and data[i+1] == '비' and data[i+2] == '갑': data[i] = '가성비갑'
    if data[i] == '퍼스' and data[i+1] == '널': data[i] = '퍼스널'
    if data[i] == '에디' and data[i+1] == '션': data[i] = '에디션'
    if data[i] == '웨' and data[i+1] == '이스트': data[i] = '웨이스트'
    if data[i] == '인터' and data[i+1] == '렉티브': data[i] = '인터렉티브'
    if data[i] == '비주' and data[i+1] == '얼': data[i] = '비주얼'
    if data[i] == '모음' and data[i+1] == '집': data[i] = '모음집'
    if data[i] == '김' and data[i+1] == '서림': data[i] = '김서림'
    if data[i] == '비' and data[i+1] == '접촉': data[i] = '비접촉'
    if data[i] == '진' and data[i+1] == '정': data[i] = '진정'
    if data[i] == '빠' and data[i+1] == '른': data[i] = '빠른'
    if data[i] == '무스' and data[i+1] == '탕': data[i] = '무스탕'
    if data[i] == '양' and data[i+1] == '말': data[i] = '양말'
    if data[i] == '필수' and data[i+1] == '템': data[i] = '필수템'
    if data[i] == '스트' and data[i+1] == '링': data[i] = '스트링'
    if data[i] == '베이' and data[i+1] == '킹': data[i] = '베이킹'
    if data[i] == '관리' and data[i+1] == '기': data[i] = '관리기'
    if data[i] == '수제' and data[i+1] == '화': data[i] = '수제화'
    if data[i] == '호박' and data[i+1] == '즙': data[i] = '호박즙'
    if data[i] == '후' and data[i+1] == '리스': data[i] = '후리스'
    if data[i] == '스타' and data[i+1] == '일리' and data[i+2] == '쉬': data[i] = '스타일리쉬'
    if data[i] == '플리' and data[i+1] == '스': data[i] = '플리스'
    if data[i] == 'Y' and data[i+1] == '존': data[i] = 'Y존'
    if data[i] == '에어' and data[i+1] == '프라이어': data[i] = '에어프라이어'
    if data[i] == '개인' and data[i+1] == '용': data[i] = '개인용'
    if data[i] == '차량' and data[i+1] == '용': data[i] = '차량용'
    if data[i] == '풍' and data[i+1] == '성하다': data[i] = '풍성하다'
    if data[i] == '배양' and data[i+1] == '액': data[i] = '배양액'
    if data[i] == '샤워' and data[i+1] == '기': data[i] = '샤워기'
    if data[i] == '집' and data[i+1] == '순이': data[i] = '집순이'
    if data[i] == '프라이' and data[i+1] == '빗': data[i] = '프라이빗'
    if data[i] == '스타' and data[i+1] == '일링': data[i] = '스타일링'
    if data[i] == '스타' and data[i+1] == '일리': data[i] = '스타일리'
    if data[i] == '플레이' and data[i+1] == '팅': data[i] = '플레이팅'
    if data[i] == '고양': data[i] = '고양이'
    if data[i] == '디' and data[i+1] == '스펜서': data[i] = '디스펜서'
    if data[i] == '아가' and data[i+1] == '일': data[i] = '아가일'
    if data[i] == '스포츠' and data[i+1] == '웨어': data[i] = '스포츠웨어'
    if data[i] == '플레이' and data[i+1] == '팅': data[i] = '플레이팅'
    if data[i] == '갓' and data[i+1] == '성비': data[i] = '가성비'
    if data[i] == '페스토' and data[i+1] == '토': data[i] = '페스토'
    if data[i] == '레터' and data[i+1] == '링': data[i] = '레터링'
    if data[i] == '닭' and data[i+1] == '가슴' and data[i+2]=='살': data[i] = '닭가슴살'
    if data[i] == '기능' and data[i+1] == '성': data[i] = '기능성'
    if data[i] == '파트너' and data[i+1] == '스': data[i] = '파트너스'
    if data[i] == 'calendar': data[i] = '캘린더'
    if data[i] == '자연' and data[i+1] == '친' and data[i+2] == '화': data[i] = '자연친화'
    if data[i] == '끝' and data[i+1] == '판' and data[i+2] == '왕': data[i] = '끝판왕'
    if data[i] == '리버' and data[i+1] == '시' and data[i+2] == '블': data[i] = '리버시블'
    if data[i] == '구' and data[i+1] == '독자': data[i] = '구독자'
    if data[i] == '인공' and data[i+1] == '지능': data[i] = '인공지능'
    if data[i] == '프로' and data[i+1] == '바이' and data[i+2] =='틱' and data[i+3] =='스': data[i] = '프로바이오틱스'
    if data[i] == '귀' and data[i+1] == '찌': data[i] = '귀찌'
    if data[i] == '귀' and data[i+1] == '여' and data[i+2] == '움': data[i] = '귀여움'
    if data[i] == '프로' and data[i+1] == '페셔널': data[i] = '프로페셔널'
    if data[i] == '에스테': data[i] = '에스테틱'
    if data[i] == '마스크' and data[i+1] == '팩': data[i] = '마스크팩'
    if data[i] == '비' and data[i+1] == '행': data[i] = '비행'
    if data[i] == '크랩' and data[i+1] == '슨': data[i] = '크랩슨'
    if data[i] == '캐' and data[i+1] == '쥬얼': data[i] = '캐주얼'
    if data[i] == '벨' and data[i+1] == '보이': data[i] = '벨보이'
    if data[i] == '카' and data[i+1] == '페' and data[i+2] == '인': data[i] = '카페인'
    if data[i] == '천' and data[i+1] == '연성': data[i] = '천연성'
    if data[i] == '그리다' and data[i+1] == '톡': data[i] = '그립톡'
    if data[i] == '킥' and data[i+1] == '보드': data[i] = '킥보드'
    if data[i] == '스테' and data[i+1] == '퍼': data[i] = '스테퍼'
    if data[i] == '행' : data[i] = '여행'
    if data[i] == '쥬얼리' : data[i] = '주얼리'
    if data[i] == '보온' and data[i+1] == '성과': data[i] = '보온성'
    if data[i] == '추리' and data[i+1] == '닝': data[i] = '추리닝'
    if data[i] == '놀이' and data[i+1] == '용': data[i] = '놀이용'
    if data[i] == '교육' and data[i+1] == '용': data[i] = '교육용'
    if data[i] == '마술' and data[i+1] == '용': data[i] = '마술용'
    if data[i] == '선물' and data[i+1] == '용': data[i] = '선물용'
    if data[i] == '바디' and data[i+1] == '필' and data[i+2] == '로우': data[i] = '바디필로우'
    #if data[i] == '바디' and data[i+1] == '필로우': data[i] = '바디필로우'
    if data[i] == '천연' and data[i+1] == '석': data[i] = '천연석'
    if data[i] == '말좀' and data[i+1] == '하자': data[i] = '말좀하자'
    if data[i] == '탈출' and data[i+1] == '구가': data[i] = '탈출구가'
    if data[i] == '무지' and data[i+1] == '갯빛': data[i] = '무지갯빛'
    if data[i] == '폭' and data[i+1] == '신': data[i] = '폭신'
    if data[i] == '발라드' and data[i+1] == '세': data[i] = '발라드세'

#for i in range(0, len(data)-1):
#    if data[i] == '바디' and data[i+1] == '필로우': data[i] = '바디필로우'

stop1 = "첼,투맨,리프,레쉬,탈출구가,깔다,끄다,밉다,무지갯빛,IN,격다른,이즈백,폭신,가성비,쩌,상상,발라드세,가성비,쩌,리미,티드,쪼,득,브릭,돈,닉,일리,브루,틴,로이,트,츠,그네,틱,슬립,산,비,갑,널,션,이스트,렉티브,얼립,집,서림,접촉,정,른,탕,말,템,링,킹,기,화,즙,리스,일리,쉬,스,존,프라이어,용,성하다,액,기,순이,빗,일링,일리,팅,스펜서,일,웨어,성비,토,링,가슴,살,성,스,친,화,판,왕,시,블,독자,지능,바이,틱,스,찌,여,움,페셔널,팩,행,손,쥬얼,보이,페,인,연성,톡,보드,퍼,성과,닝,용,필로우,석,나,싹,내,굿,확,착,더,너,그,을,를,빠삭,더빠삭,도,안녕,듯,그냥,이제,탓,것,왜,어디,진짜,쫀득꾸덕,촉촉꼬소,또,뭐,오늘,의,완전,주년,마지막,부터"
stop2 = "필,말좀하자,로우,하나,모두,과,그대로,지금,비로소,반드시,똭,꽉,혁명,당신,모든,한번,유독,우리,바로,단,제,언제,외,아무,이번,득템,미미,가능,정석,쓱쓱,직접,대박,뇌피셜,오직,다시,해도,거기,쫀쫀,정말,신기록,신제품,마치,첫,『,어디서,누가,이렇게,더욱,대에,혹시,자장자장,제발,이하,한껏,계절,동안,수고,손색,일당백,알짜,배기,지름길,찌릿찌릿,때문,방출,핏빛,살벌,쓱,쏙,껄,물씬,제대로,땡큐,승자,오기,생템,간단,낱,덩이,상황,먼저,편견,대기존에,무난,신자,소취,찰떡,오구오구,푹푹,정신없이,끝판,끝판왕,품평,하반기,꼭,쫀득,로만,좌르르,뺨,불땐,듬뿍,월요일,리미트,살짝,수령,난리,껏,유닉템,사모님,대비,더하기,동영상,존잘,각각,확장,금쪽,장인정신,뽀송뽀송,충만,최애,견줄,중반,상세,납작,계속,감탄,어제,짊어,성해,집앞,본연,나야,에브리데이,가려진,비갑,큐티,큰일,하지만,동반,디자인,사람과,앙리마티스,따로,장인과,신화평점,두둥,우린,질적,달라,VIP,즌,일단,남아,미스터"
stop3 = "챔피언,대상,뿌리,뚝딱,포로,더블,암시,펀딩하,당장케첩,당장,밥말,과연,닷,쪽,일만,set,평점,쫙,요청,처음,죽지,은밀,이면,인연,요즘,라며,유행,뭐길,춤,물질,데,퐁당,통째,반값,최초,룰루,결국,제법,어떤,th,누군가,요,마스터,에브리,역대,불과,경계,이징,집의,주말,득,바삭,먼트,잔잔,마우스,달성,뉴,극찬,무한,초월,먹기,원대,간결,날수,기회,하니,모의,번째,PICK,히든,물론,H,접목,백앵콜,루트,예약,낼,최고,내년,여름날,린다,고민,레알,특히,풍성하다,손씻다,폭,엉,가성비,만에,돈,인터랙티브,빠른,년,가지,시간,원,근,분,개,mm,일,포,L,천만원,kcal,개월,병,ppm,명,등,끼,톤,만점,대,다,종,점,차,인치,° c,캐럿,g,배,짜리,온스,ver,마리,초,인분,대,수,권,장,부,가정,주기,집,명의,부촌,륜,만,호,만원,억,kg"

stop = stop1+","+stop2+","+stop3

stop_list = stop.split(',')

def stopword(word_tokenize) :
    result = []

    for w in word_tokenize :
        if w not in stop_list :
            result.append(w)
    return result

print("수정결과")
print(str(stopword(data)))


conn.close()
