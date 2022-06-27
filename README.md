
# payments-survey

sm --start OPS_ACCEPTANCE -r


run with this command if you want to avoid the memory leak:
sbt -mem 2048 clean test compile

Post localhost:9966/payments-survey/journey/start
{"origin":"lala","returnMsg":"Return to gov uk","returnHref":"https://www.gov.uk","auditName":"auditname","audit":{"userType":"IsLoggedIn"},"contentOptions":{"isWelshSupported":true,"title":{"englishValue":"Pay your tax","welshValue":"Talu treth"}}}

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
