## はじめに

javafx scaffolding, JDK17 + JavaFX17 + controlsfx 11.x.x + Maven

frameとloginは基本モジュールで、Java SPI経由でプラグイン可能で、アプリケーションモジュールの統合が容易です。 デモはサンプルモジュールです。

## 構造

- docs: ドキュメンテーション (docsifyを使って構築)
- frame: アプリケーション本体のUIフレームワーク (SPIインターフェースを提供)
- core: アプリケーションのコアコンポーネント。
- login: ログインモジュール (SPIインターフェースを提供)
- demo: デモ例 (controlsfxのデモ修正に基づく)
- common: 共通コンポーネントモジュール。
- smc, qe, cg: 個人用アプリケーション (参照不要)

## インターフェース

### core

- TemplateLoaderService: freemarker TemplateLoaderの読み込み，サブモジュールのテンプレートパスを追加するサブクラスの実装
- GroovyLoaderService: Groovy スクリプト パスの読み込み、サブモジュール スクリプト パスを追加するためのサブクラス実装

### frame

- FXSamplerProject: project信息，包含project名，模块，包名，欢迎页等
- MenubarConfigration: メニューバーの設定
- FXSamplerConfiguration: プロジェクトのスタイル、タイトル、アイコンの設定
- CenterPanelService: クリックおよびスイッチング・コンポーネントのインターフェイスを含む、中央エリアのノード構成
- SplashScreen: スプラッシュスクリーンの写真
- SamplePostProcessorService: Sampleの後処理サンプル
- VersionCheckerService: バージョン更新チェック
- SamplesTreeViewConfiguration: メニューツリーの構成

### login

- LoginCheck: ログイン認証

## パッケージ化

1. IDEA アーティファクトのパッケージ化\
構成：プロジェクト構造 -> アーティファクト -> JARを追加 -> ターゲットJARに展開 -> 独自のマニフェスト・ファイル・パスを選択 -> 完了\
パッケージング：ビルド -> アーティファクトのビルド -> ビルド
2. 「`mvn package`」を通じてパッケージ化することをお勧めします

## リエゾン

ご不明な点がございましたら、liang.tang.cx@gmail.com までメールでお問い合わせください。

## ありがたい

- <a href="https://jb.gg/OpenSource"><img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png?_gl=1*98642y*_ga*MTIxMDA5OTM5Ni4xNjgwMzQyNjgy*_ga_9J976DJZ68*MTY4MTIxMDIzMy41LjEuMTY4MTIxMTE1MS4wLjAuMA..&_ga=2.268101710.1369693703.1681210234-1210099396.1680342682" width="100px" alt="jetbrains">
- **JetBrainsの無償オープンソース・ライセンスに感謝する。**</a>
