package com.franksap2.finance.datasource.mocks.profile

import org.intellij.lang.annotations.Language

@Language("JSON")
internal val stockInfoJson = """
{
    "logo": "https://s3.polygon.io/logos/aapl/logo.png",
    "listdate": "1990-01-02",
    "cik": "320193",
    "bloomberg": "EQ0010169500001000",
    "figi": null,
    "lei": "HWUPKR0MPOU8FGXBT394",
    "sic": 3571,
    "country": "usa",
    "industry": "Computer Hardware",
    "sector": "Technology",
    "marketcap": 908316631180,
    "employees": 123000,
    "phone": "+1 408 996-1010",
    "ceo": "Timothy D. Cook",
    "url": "http://www.apple.com",
    "description": "Apple Inc is designs, manufactures and markets mobile communication and media devices and personal computers, and sells a variety of related software, services, accessories, networking solutions and third-party digital content and applications.",
    "exchange": "Nasdaq Global Select",
    "name": "Apple Inc.",
    "symbol": "AAPL",
    "exchangeSymbol": "NGS",
    "hq_address": "1 Infinite Loop Cupertino CA, 95014",
    "hq_state": "CA",
    "hq_country": "USA",
    "type": "CS",
    "updated": "11/16/2018",
    "tags": [
        "Technology",
        "Consumer Electronics",
        "Computer Hardware"
    ],
    "similar": [
        "MSFT",
        "NOK",
        "IBM",
        "HPQ",
        "GOOGL",
        "BB",
        "XLK"
    ],
    "active": true,
    "profile": {
        "country": "US",
        "currency": "USD",
        "estimateCurrency": "USD",
        "exchange": "NASDAQ NMS - GLOBAL MARKET",
        "finnhubIndustry": "Technology",
        "ipo": "1980-12-12",
        "logo": "https://static2.finnhub.io/file/publicdatany/finnhubimage/stock_logo/AAPL.png",
        "marketCapitalization": 2918338.1599479564,
        "name": "Apple Inc",
        "phone": "14089961010",
        "shareOutstanding": 15037.87,
        "ticker": "AAPL",
        "weburl": "https://www.apple.com/"
    },
    "recommendations": [
        {
            "buy": 23,
            "hold": 12,
            "period": "2025-04-01",
            "sell": 3,
            "strongBuy": 12,
            "strongSell": 1,
            "symbol": "AAPL"
        },
        {
            "buy": 25,
            "hold": 12,
            "period": "2025-03-01",
            "sell": 3,
            "strongBuy": 12,
            "strongSell": 1,
            "symbol": "AAPL"
        },
        {
            "buy": 25,
            "hold": 13,
            "period": "2025-02-01",
            "sell": 3,
            "strongBuy": 12,
            "strongSell": 1,
            "symbol": "AAPL"
        },
        {
            "buy": 26,
            "hold": 13,
            "period": "2025-01-01",
            "sell": 2,
            "strongBuy": 14,
            "strongSell": 0,
            "symbol": "AAPL"
        }
    ]
}
""".trimIndent()