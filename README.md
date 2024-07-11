# Dungeons & Dragons

An app to help me play Dungeons & Dragons.

[![Run Build](https://github.com/brandoncorrea/dnd-app/actions/workflows/build.yml/badge.svg)](https://github.com/brandoncorrea/dnd-app/actions/workflows/build.yml)

## Setup

    # clojure
    brew install clojure

    # java
    I don't remember how to install this on MacOS.
    Use Java 21

## Commands

    # watch for css changes
    clj -M:test:css

    # build cljs on-change (development)
    clj -M:test:cljs

    # build cljs once (deployment)
    clj -M:test:cljs once

    # watch for both css/cljs changes and run specs
    clj -M:test:dev-

    # see test coverage
    clj -M:test:coverage

## Deployment

    # Stage
    git checkout stage
    git merge master
    git push
    git checkout master

    # Production
    git checkout production
    git merge stage
    git push
    git checkout master
