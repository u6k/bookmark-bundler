# bookmark-bundler

[![CircleCI](https://img.shields.io/circleci/project/github/u6k/bookmark-bundler.svg)](https://circleci.com/gh/u6k/blog)
[![license](https://img.shields.io/github/license/u6k/bookmark-bundler.svg)](https://github.com/u6k/bookmark-bundler/blob/master/LICENSE)
[![GitHub tag](https://img.shields.io/github/tag/u6k/bookmark-bundler.svg)](https://github.com/u6k/bookmark-bundler/releases)
[![Docker Pulls](https://img.shields.io/docker/pulls/u6kapps/bookmark-bundler.svg)](https://hub.docker.com/r/u6kapps/bookmark-bundler/)

ブックマークを束ねる(管理する)サービスを構築します。

## Requirement

* Docker

```
Client:
 Version:      17.03.1-ce
 API version:  1.27
 Go version:   go1.7.5
 Git commit:   c6d412e
 Built:        Tue Mar 28 00:40:02 2017
 OS/Arch:      windows/amd64

Server:
 Version:      17.04.0-ce
 API version:  1.28 (minimum version 1.12)
 Go version:   go1.7.5
 Git commit:   4845c56
 Built:        Wed Apr  5 18:45:47 2017
 OS/Arch:      linux/amd64
 Experimental: false
```

以下は、開発用Dockerイメージに含まれます。

* Java SDK

```
$ java -version
java version "1.8.0_102"
Java(TM) SE Runtime Environment (build 1.8.0_102-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.102-b14, mixed mode)
```

* Apache Maven

```
$ mvn -v
Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-11T01:41:47+09:00)
Maven home: C:\tools\apache-maven-3.3.9
Java version: 1.8.0_102, vendor: Oracle Corporation
Java home: C:\Program Files\Java\jdk1.8.0_102\jre
Default locale: ja_JP, platform encoding: MS932
OS name: "windows 10", version: "10.0", arch: "amd64", family: "dos"
```

* aglio
* plantuml

## Build

### 開発環境をセットアップ

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

### 動作確認

```
docker run \
    --rm \
    -p 8080:8080 \
    -v $HOME/.m2:/root/.m2 \
    -v $(pwd):/var/my-app \
    u6kapps/bookmark-bundler-dev mvn clean spring-boot:run
```

### 実行用イメージを作成

jarファイルを作成します。

```
docker run \
    --rm \
    -v $HOME/.m2:/root/.m2 \
    -v $(pwd):/var/my-app \
    u6kapps/bookmark-bundler-dev mvn clean package
```

実行用イメージを作成します。

```
docker build -t u6kapps/bookmark-bundler .
```

## Installation

### 実行

```
docker-compose up -d
```

## Author

* [bookmark-bundler - u6k.Redmine()](https://redmine.u6k.me/projects/bookmark-bundler)
* [u6k/bookmark-bundler - GitHub](https://github.com/u6k/bookmark-bundler)
* [u6k.Blog()](http://blog.u6k.me/)

## License

* [MIT License](https://github.com/u6k/bookmark-bundler/blob/master/LICENSE)
