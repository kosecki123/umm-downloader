# umm-downloader

One purpose tool, download whole UMM dataset from Nordpool page. Workarounds various issues with page like timeouts and other errors.

Tool split the given date range into 7 days periods, process downloads with 4 thread pool, merge results into single CSV file.

## Installation

Download and install Lein script from http://leiningen.org/, make sure you have Java installed

    git clone https://github.com/kosecki123/umm-downloader.git
    cd umm-downloader


## Usage

Running application without arguments downloads UMM dataset from the beginning till now.

        lein run

Optionally you can specify start and end date by using args

        lein run -- --start "2015-02-02" --end "2015-01-01"


## License

Copyright © 2016 Piotr Kosiński

Distributed under the Eclipse Public License either version 1.0 or any later version.
