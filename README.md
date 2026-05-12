
# payments-survey

Start dependant services: 

```
sm2 --start OPS_ACCEPTANCE -r
```


run with this command if you want to avoid the memory leak:
```
sbt -mem 2048 clean compile run
```

Start a journey with this request: 

```
curl -X POST localhost:9966/payments-survey/journey/start -H "Content-Type: application/json" -d '{
  "origin": "lala",
  "returnMsg": "Return to gov uk",
  "returnHref": "https://www.gov.uk",
  "auditName": "auditname",
  "audit": {
    "userType": "IsLoggedIn",
    "journey": "PaymentComplete",
    "orderId": "123",
    "liability": "Self Assessment",
    "surveySource": "pay-frontend",
    "paymentId": "ecospend payment id",
    "origin": "ptaSa"
  },
  "contentOptions": {
    "isWelshSupported": true,
    "title": {
      "englishValue": "Pay your tax",
      "welshValue": "Talu treth"
    }
  }
}'
```

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
