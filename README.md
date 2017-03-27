# bookmark-bundler

ブックマークを束ねる(管理する)サービスを構築します。

## 開発環境をセットアップ

`Dockerfile-dev`を使用して開発用イメージを作成します。

```
docker build -t u6kapps/bookmark-bundler-dev -f Dockerfile-dev .
```

Eclipseプロジェクトを作成するには、開発用イメージを起動して`mvn eclipse:eclipse`を実行します。

```
docker run \
    --rm \
    -v $HOME/.m2:/root/.m2 \
    -v $(pwd):/var/my-app \
    u6kapps/bookmark-bundler-dev mvn eclipse:eclipse
```

## 動作確認

```
docker run \
    --rm \
    -p 8080:8080 \
    -v $HOME/.m2:/root/.m2 \
    -v $(pwd):/var/my-app \
    u6kapps/bookmark-bundler-dev mvn clean spring-boot:run
```

## 動作用イメージを作成

jarファイルを作成します。

```
docker run \
    --rm \
    -v $HOME/.m2:/root/.m2 \
    -v $(pwd):/var/my-app \
    u6kapps/bookmark-bundler-dev mvn clean package
```

動作用イメージを作成します。

```
docker build -t u6kapps/bookmark-bundler .
```

## 実行

```
docker run \
    -d \
    --name bookmark \
    -v $HOME/docker/bookmark/hsqldb:/var/lib/bookmark/hsqldb \
    -p 8080:8080 \
    u6kapps/bookmark-bundler
```
