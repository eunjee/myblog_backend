# falsylog_backend

## 技術スタック
- Java
- Spring
- Spring Boot
- Spring Data Jpa
- Spring Security
- Spring Rest Docs
- Jenkins
- AWS

## 目標
- TDD, BDDの練習
  - Red -> Green -> Refactorに集中
- 自分のためのブログページの作成
- サービス開発〜運営までの経験蓄積
- 機能追加、改善、補修等の経験蓄積
- commitをもっと細かくやる
- CI/CDの経験

## commits
### 2023.10.16
- [x] ポスト作成APIの追加
  - [x] 統合テスト
  - [x] Business Layerテスト
  - [x] Persistence Layerテスト
### 2023.10.20
- [x] Spring Rest Docsの導入
  - [x] ポスト作成DOC Testを作成 (post-create)
  - [x] ポスト一覧DOC Testを作成 (post-list)
  - [x] ポスト一覧API作成
  - [x] ポスト一覧 Controller Test作成
  - [x] ポスト一覧 Service Test作成
### 2023.10.21
- [x] ポスト詳細照会API作成
  - [x] ポスト詳細照会 統合テスト
  - [x] ポスト詳細照会 Serviceテスト
  - [x] ポスト詳細照会 Repositoryテスト
### 2023.10.26
- [x] ポスト詳細照会DOCテスト作成 (getPost)
- [x] ポスト削除DOCテスト作成 (deletePost)
### 2023.10.27
- [x] ポスト登録にValidationをかける (post-create)
- [x] Error専用レスポンスオブジェクトの生成
- [x] Validationに対するテスト作成
- [x] レスポンスに作成日,更新日追加
### 2023.10.28
- [x] レスポンスに作成日,更新日テスト作成
- [x] レファレンス用資料であるBaseEntityのフィルドをビジニス用カラムと使用するのを廃止
### 2023.11.02
- [x] HashTag機能追加
### 2023.11.04
- [x] ポスト登録時のエラレスポンスのAPIDocs作成
- [x] クライアント要求によるエラレスポンスからcodeフィルドの削除
### 2023.11.05
- [x] HashTagをクリックして関連する該当のポストリストを返す。