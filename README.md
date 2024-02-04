# myblog_backend

- myblog_frontend
  -  https://github.com/hjyang369/my-blog

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
### 2023.12.30
- [x] post update機能実装
- [x] post updateテスト実装
### 2023.12.29
- [x] @EventListenerの導入
- [x] ポスト削除テストの追加
- [x] profileの分離
- [x] bug fix
### 2023.12.14
- [x] ポストコントローラーAPI文書最新化、例外レスポンス登録
### 2023.12.13
- Rest DOCS TESTを使ってAPI文書を作るのにSpring Containerを立てる必要まではないなのでMockistを使うように修正する
### 2023.12.03
- [x] method securityの追加
### 2023.11.29
- [x] spring-securityを入れる
### 2023.11.28
- [x] password暗号化
### 2023.11.27
- [x] 新規入会機能仮実装
### 2023.11.26
- [x] 認証方式としてjwt方式も追加
### 2023.11.25
- [x] Session Cookie方式を利用したログイン機能の実装
### 2023.11.23
- [x] Snippet Custom 適用
- [x] aws ec2 deploy
### 2023.11.22
- [x] swagger -> asciidoc
### 2023.11.20
- [x] paging追加
  - フロントからの要求事項
    - `size=5`, ページ番号は１からスタート
- [x] apiの返り値の言語は基本韓国語に変更
  - フロントからの要求事項
### 2023.11.19
- [x] システム全体例外追加、PostNotFound, TagNotFound例外追加
### 2023.11.15
- [x] Hashタグをクリックすると関連ポストと格ポストに関連付けられたハッシュタグも一緒に返す。
### 2023.11.05
- [x] HashTagをクリックして関連する該当のポストリストを返す。
### 2023.11.04
- [x] ポスト登録時のエラレスポンスのAPIDocs作成
- [x] クライアント要求によるエラレスポンスからcodeフィルドの削除
### 2023.11.02
- [x] HashTag機能追加
### 2023.10.28
- [x] レスポンスに作成日,更新日テスト作成
- [x] レファレンス用資料であるBaseEntityのフィルドをビジニス用カラムと使用するのを廃止
### 2023.10.27
- [x] ポスト登録にValidationをかける (post-create)
- [x] Error専用レスポンスオブジェクトの生成
- [x] Validationに対するテスト作成
- [x] レスポンスに作成日,更新日追加
### 2023.10.26
- [x] ポスト詳細照会DOCテスト作成 (getPost)
- [x] ポスト削除DOCテスト作成 (deletePost)
### 2023.10.21
- [x] ポスト詳細照会API作成
  - [x] ポスト詳細照会 統合テスト
  - [x] ポスト詳細照会 Serviceテスト
  - [x] ポスト詳細照会 Repositoryテスト
### 2023.10.20
- [x] Spring Rest Docsの導入
  - [x] ポスト作成DOC Testを作成 (post-create)
  - [x] ポスト一覧DOC Testを作成 (post-list)
  - [x] ポスト一覧API作成
  - [x] ポスト一覧 Controller Test作成
  - [x] ポスト一覧 Service Test作成
### 2023.10.16
- [x] ポスト作成APIの追加
  - [x] 統合テスト
  - [x] Business Layerテスト
  - [x] Persistence Layerテスト

### 2024.01.27
- [x] Post Entity 수정
  - [x] 중복되는 createdDate 필드 삭제
  - [x] @PrePersist 사용 createdAt을 입력받을 수 있도록 수정 (null일 경우 현재 시간으로 설정)
- [x] Member별로 작성한 게시글 목록 조회 기능 추가
  - [x] 테스트 코드 작성 완료
  - 
### 2024.01.31
- [x] 회원별 게시글 조회 API 구현 ("/member/{name}/posts")
  - [x] Repository Test Code

### 2024.02.04
- [x] Resume Entity 수정
  - CaseCadeType 정리 필요
- [x] 파일 업로드, 조회, 삭제 API 구현
  - 업로드 시 validation 필요
  - 업로드 시 경로 설정 필요

