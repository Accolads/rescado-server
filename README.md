# rescado-server

## Development

To run `rescado-server` for development, you'll need the following config for a `rescado_loc` PostgreSQL database with PostGIS extension running locally on port 5432:
```postgresql
psql -c "CREATE ROLE rescado_user WITH LOGIN superuser PASSWORD 'rescado_password';"
psql -c "CREATE DATABASE rescado_loc WITH OWNER rescado_user;"
psql -c "CREATE EXTENSION postgis;"
```

Alternatively, if you have Docker installed, you can spin up a container with working database config with this command:
```shell
docker-compose run -p 5432:5432 -e POSTGRES_DB='rescado_loc' rescado-store
```
