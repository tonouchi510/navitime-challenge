![image](https://user-images.githubusercontent.com/19544971/90310126-8d918980-df29-11ea-8dec-07a3f3e67af4.png)

# navitime-challenge

ハッカソンアプリ案：「スキマ時間の宅配シフト提案アプリ（仮）」

開発スケジュール：[スプレッドシートの開発計画](https://docs.google.com/spreadsheets/u/1/d/1JADgCvRZBICOabYKJMkdCKYy0vVKmNjFMdeH54cQFNw/edit?usp=drive_web&ouid=115497469233801365613)

## アプリの機能  
- ユーザのスキマ時間に可能なシフトを提案する
  - Google Map/Calenderと連携して、"ユーザの現在地"と"注文状況（店・配達先の位置情報）"から、スキマ時間で最大利益を生むシフトを組む
  - 複数の目的地を含む中からの最適経路（最も稼げるシフト）は、NAVITIMEの最適経路探索APIを利用して求める
  - 配達注文アプリの方はモックサービスを作る
- 音声による操作
  - 配達員は自転車などに乗っていることが想定されるので、音声操作が可能だと利便性が上がる
    - ウーバー配達員は手袋つけてることも多い？からスマホ操作もできない
    - pixel4のジェスチャー機能でやろうと思ったが、日本版は実装されないため断念（→音声操作に切り替え）
    

## アプリが解決する問題

昨今話題のウーバーイーツなどのCtoCの宅配注文サービスは、現状では**配達員不足**で今一歩普及しきれていない。
今回のアプリは配達員のスキマ時間で稼げるシフトを自動で提案するので、配達員による受注の流れを促進できると思う。

※これでCtoC配達の文化が広まり、どんどん便利な世の中になってくれると嬉しい

- 配達員：スキマ時間を有効活用できる
- 利用者：配達員が増えることで、自分の注文が受注されやすくなる
