FORMAT: 1A

# bookmark-bundler API

`bookmark-bundler`アプリケーションのREST API仕様です。

# Group Bookmark

Bookmarkは、そのままブックマークです。

BookmarkはUUIDを識別子とします。同一URLのBookmarkは許容しません。

## Bookmark Collection [/bookmarks]

### 全てのBookmarkを取得 [GET]

Bookmarkを全て取得します。更新日の降順に返します。

Bookmarkが0個の場合、空配列`[]`を返します。

+ Response 200 (application/json)

        [
            {
                "id":"f45b94dc-74f3-4581-b4bd-d96fbcc9a101",
                "name":"bookmark-bundler API仕様",
                "url":"https://u6k.github.io/bookmark-bundler/"
            },
            {
                "id":"5b5f872d-f88c-4e25-b4e0-ec97f381b2aa",
                "name":"CircleCI",
                "url":"https://circleci.com/gh/u6k/bookmark-bundler"
            }
        ]

### Bookmarkを新規登録 [POST]

名前、URLを指定して、Bookmarkを新規登録します。

成功した場合、`201 Created`と、Bookmarkの内容を返します。

名前、URLが空の場合、`400 Bad Request`を返します。同一URLが既に存在する場合も、`400 Bad Request`を返します。

+ Request (application/json)

        {
            "name":"u6k.Blog()",
            "url":"http://blog.u6k.me/"
        }

+ Response 201 (application/json)

        {
            "id":"acf1c578-c562-42ca-a2a2-d9da6ac19d0a",
            "name":"u6k.Blog()",
            "url":"http://blog.u6k.me/"
        }

## Search Bookmark [/bookmarks?keyword={keyword}]

+ Parameters
    + keyword: github (string, optional) - キーワード

### キーワードを指定してBookmarkを検索 [GET]

キーワードを指定して、Bookmarkを検索します。更新日の降順に返します。

キーワードが空の場合、空で検索します。

検索結果が0個の場合、空配列`[]`を返します。

+ Response 200 (application/json)

        [
            {
                "id":"f45b94dc-74f3-4581-b4bd-d96fbcc9a101",
                "name":"bookmark-bundler API仕様",
                "url":"https://u6k.github.io/bookmark-bundler/"
            }
        ]

## Bookmark [/bookmarks/{bookmark_id}]

+ Parameters
    + bookmark_id: `acf1c578-c562-42ca-a2a2-d9da6ac19d0a` (uuid) - Bookmarkの識別子。指定したidのBookmarkが存在しない場合、`404 Not Found`を返します。

### Bookmarkを取得 [GET]

Bookmarkを取得します。

+ Response 200 (application/json)

        {
            "id":"acf1c578-c562-42ca-a2a2-d9da6ac19d0a",
            "name":"u6k.Blog()",
            "url":"http://blog.u6k.me/"
        }

### Bookmarkを変更 [PUT]

Bookmarkの内容を変更します。

リクエストはBookmarkの全ての項目を指定してください。指定されない項目は未設定になります。必須項目を指定しなかった場合、`400 Bad Request`を返します。設定しようとしたURLが既に存在する場合、`400 Bad Request`を返します。

+ Request (application/json)

        {
            "name":"u6k.Redmine()",
            "url":"http://redmine.u6k.me/"
        }

+ Response 200 (application/json)

        {
            "id":"acf1c578-c562-42ca-a2a2-d9da6ac19d0a",
            "name":"u6k.Redmine()",
            "url":"http://redmine.u6k.me/"
        }

### Bookmarkを削除 [DELETE]

Bookmarkを削除します。

+ Response 204
