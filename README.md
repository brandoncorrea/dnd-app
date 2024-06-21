# D&D Website

An app to help me play Dungeons & Dragons.

## Setup

    # clojure
    brew install clojure

    # node
    brew install npm

    # http-server
    npm i http-server -g

    # NPM Install
    npm i

## Commands

    # Watch for css changes
    clj -Mcss

    # watch for both css/cljs changes and run specs
    clj -Mtest:dev-

    # build cljs on-change (for development)
    clj -Mcljs auto production

    # build cljs once (for production)
    clj -Mcljs once production

## Sandbox

### Configuration

The sandbox pages will only appear in the `development` environment.
To show them, update `main.cljs` with the `"development"` configuration.

For production, you will want these hidden. Update `main.cljs` to start up with `"production"`.

### Playing in the Sandbox

A sandbox page is just an implementation of `page/render`.

1. Create a cljs file under `src/cljs/dnd/sandbox` - call it whatever you'd like!

```clojure
(ns dnd.sandbox.example
  (:require [dnd.page :as page]))

(defmethod page/render :sandbox/example [_]
  ; Your hiccup code here...
  )
```

2. Add your new namespace to `dnd.sandbox.core`

```clojure
(ns dnd.sandbox.core
  (:require ;...namespaces... 
            [dnd.sandbox.example]
            ;...namespaces...
    ))
```

3. Create a test for your new sandbox in `dnd.router-spec`

```clojure
(context "sandbox" 
  ; ...specs... 
  (it-routes "/sandbox/example" :sandbox/example)
  ; ...more specs...
  )
```

4. Pass your test in `dnd.router`

```clojure
(defn def-sandbox []
  ; ...routes...
  (defroute "/sandbox/example" [] (page/install! :sandbox/example))
  ; ...more routes...
  )
```

Note: Your `:sandbox/keyword` will need to exactly match your route: `"/sandbox/keyword"`.

## Deployment

    # Build cljs
    clj -Mcljs once production

    # Build css
    clj -Mcss once

## Local HTTP Server

    bin/server
