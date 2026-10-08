# Cat Facts API (CSV edition)

A small JSON API for cat facts. Facts are stored in `facts.csv`.

## Running it

1. Open **this folder** (`cat-facts-starter`) in VS Code, not a folder above it.
2. Open the Run and Debug panel and press play on **Run Cat Facts API**.

You should see:

```
Loaded 10 facts from facts.csv
Cat Facts API running on http://localhost:8080/facts
```

To stop the server, press the red stop square. Stop it before pressing play again.

## Trying it

```sh
curl http://localhost:8080/facts
curl http://localhost:8080/facts/3
curl -X POST http://localhost:8080/facts -d '{"fact": "Cats have three eyelids"}'
```

| Request | Success | Errors |
|---|---|---|
| `GET /facts` | 200, a JSON array of every fact | |
| `GET /facts/{id}` | 200, one fact | 404 if no such id, 400 if the id is not a number |
| `POST /facts` | 201, the created fact | 400 if the body is not `{"fact": "..."}` or the fact is blank |

Requires JDK 25. 
