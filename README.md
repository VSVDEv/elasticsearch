# ElasticSearch
![logo](bannerblue.jpg)
## Docker [link to folder](docker)
go to folder and run
docker-compose -f docker-compose.yml up -d
to stop 
docker-compose down

go to check
http://localhost:9200
kibana
http://localhost:5601

### Spring rest  [link to folder](springrest)
work with postman [link to folder](postman)

collection ElasticSearch:
run articles

run App

postman: 
App collection 

run /ping

run /articles 

with different title and text variables 

run /search
and you can change query value

## Examples [link to folder](examples)
run Docker then run tests

## ES Spring Data  [link to folder](es_spring_data)
run Docker then create index "vsvdev" then  run app


## Link Rating - A link collector and rating app [link to folder](es_link_raiting)

This is a test application using Spring Boot & Elasticsearch in order to
collect Links and display them based on rating and age.

The intention of this app is to be able to run it yourself locally and play
around with Elasticsearch and its scoring.



### Requirements

You need to create a github oauth app. You can do so at
https://github.com/settings/developers

Next step is to spin up an Elasticsearch instance. You can use [Elastic
Cloud](https://cloud.elastic.co/), https://app.bonsai.io/signup *** free 3 node, or docker compose for this or alternatively just spin up an
instance locally and use that one for testing.

Next up is to set the proper environment variables before starting up. I
highly encourage you to use something like `direnv` and then place the
variables in the `.env` file. The following environment variables are
required

* `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENT_ID=...`
* `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENT_SECRET=...`
* `ELASTICSEARCH_URL=https://user:password@your-elasticsearch-node:9200`
* `ADMINS="spinscale,xeraa"`
* `ELASTIC_APM_SERVICE_NAME=linkrating`
* `ELASTIC_APM_SECRET_TOKEN=THE_SECRET_TOKEN` (optionally)
* `ELASTIC_APM_SERVER_URLS=http://localhost:8200`
* `ELASTIC_APM_ENVIRONMENT=dev` (only set it to `prod` when deploying)

The `ADMINS` variable contains a list of github logins that are allowed to
approve links, which brings us the app functionality.

Note, that the elasticsearch user above needs to write to the `users` index,
the `links` alias and will also create an index mapping, and an index
lifecycle policy, which is used for the links being added. So it needs the
`manage_index_template` and `manage_ilm` cluster privileges - you could also
add the template and the ILM policy manually and would not need that
privilege. You also need to have index privileges for the `links*` and
`users` index, namely `read`, `write`, `create_index` and
`view_index_metadata`.

### Application Features

When starting up the app and elasticsearch and github oauth are properly
configured, you can open up a browser in the `/` URL and will see a list of
links, that are read from Elasticsearch. The interesting part is the sorting
of those links, as this is based on the number of votes and the age of the
added link. Newer links or higher votes links will appear higher and as they
age out will score lower.



If you are not logged in, there is basically only two options. You can either
watch the main page, execute a search in the top or watch a link detail page
(which features a stable URL you can link to).



Things are slightly different, once you choose to login via github. Now you
can vote for each link. However each link can only be voted once per user.



In case you are wondering why there is another icon. Links can also be
deleted, if you are an administrator.

Once you are logged in, you can also submit new links by clicking `Submit
Link` at the top. New links will not be added to the list, unless they have
been approved by an administrator user, who can visit the `/unapproved` URL.



