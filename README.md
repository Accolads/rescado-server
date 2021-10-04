# rescado-server

## Development

To run `rescado-server` for development, you'll need the following config for a `rescado_loc` PostgreSQL database running locally:
```postgresql
psql -c "CREATE ROLE rescado_user WITH LOGIN superuser PASSWORD 'rescado_password';"
psql -c "CREATE DATABASE rescado_loc WITH OWNER rescado_user;"
```

Alternatively, if you have Docker installed, you can spin up a container with working database config with this command:
```shell
docker-compose run -e POSTGRES_DB='rescado_loc' --service-ports rescado-store
```
