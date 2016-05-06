# metrics-server

This project is a server that allows clients to store and query metrics via a REST API.
The REST API is documented via Swagger (available at http://clj-metrics-server.herokuapp.com/swagger-ui/index.html).

There is a single route exposed to allow the client to POST metrics to the server.
This route accepts an array of metric objects, so the client can post as many metrics as needed in a single request.

In addition, there are several routes expose to allow the client to query metrics values.
There are 5 routes for querying a single metric's aggregate values (sum, avg, count, min, max).
It is up to the client to choose which of these aggregate values to query for a particular metric.

The server uses the Clojure [Luminus framework][1].
The data store chosen to back this server is [PostgreSQL][3].

generated using Luminus version "2.9.10.47"

[1]: http://www.luminusweb.net

## Prerequisites

You will need [Leiningen][2] 2.0 or above and [PostgreSQL][3] installed.

[2]: https://github.com/technomancy/leiningen

[3]: http://www.postgresql.org/

## Running

To start a web server for the application, run:

    lein run

Make sure that both PostgreSQL is running and that the DATABASE_URL environment variable is pointing at it before starting the application.

## License

Copyright © 2016 FIXME
