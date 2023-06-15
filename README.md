# Bloomberg

This application job is  to accept deal details and persist them into DB.

request json:
{
    "sourceCurrencyISO":"PTE",
    "targetCurrencyISO":"RUR",
    "timestamp":"2022-05-11",
    "amount":50000
}

response json:
{
    "status": "success",
    "deal": {
        "id": 3,
        "sourceCurrencyISO": "PTE",
        "targetCurrencyISO": "RUR",
        "timestamp": "2022-05-11T00:00:00.000+00:00",
        "amount": 50000.0
    }

Bad Request json :
{
    "status": "error",
    "errors": [
        {
            "errorCode": 400,
            "errorDesc": "Invalid currency ISO from the given inputs source currency iso: PTER or target currency ISO:RUR"
        }
    ]
}

{
    "status": "error",
    "errors": [
        {
            "errorCode": 400,
            "errorDesc": "Source Currency ISO is required "
        }
    ]
}

{
    "status": "error",
    "errors": [
        {
            "errorCode": 400,
            "errorDesc": "Target Currency ISO is required "
        }
    ]
}
