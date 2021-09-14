# Reduced Price Dresses Web Service

The project is built using Java 11, SpringBoot, Lombok, Wiremock, Mockito and Gradle.

## Approach
The path to get the reduced price dress is `/dresses/reduced`. 

The curl commands for the 4 different label types are:
1. `curl -s 'http://localhost:8080/dresses/reduced' | jq`  
   for the __default__ labelType `ShowWasNow`
2. `curl -s 'http://localhost:8080/dresses/reduced?labelType=ShowWasNow' | jq`  
   for the labelType `ShowWasNow`
3. `curl -s 'http://localhost:8080/dresses/reduced?labelType=ShowWasThenNow' | jq`  
   for the labelType `ShowWasThenNow`
4. `curl -s 'http://localhost:8080/dresses/reduced?labelType=ShowPercDscount' | jq`  
   for the labelType `ShowPercDscount`

I have created 2 sets of domain objects.
1. package `com.davidhay.jlassignment.domain.inbound`
These represent data coming **into** this application from JL product catalog.
2. package `com.davidhay.jlassignment.domain.outbound`
These represent data going **out** this application via `/dresses/reduced` endpoint.
These POJO names tend to end in 'Info'.

I used `BigDecimal` to store monetary amounts.

I noticed that the JL's product catalog 'now' data can sometimes be a number OR an object.
This is handled in the `NowDeserializer` class.

I used `Mockito` (a lot) for testing interactions with dependencies.

I used `Wiremock` to simulate the actual product server for Integration Tests where stub files define __canned__ responses.

I do not store the api key in my code - I've treated it like a secret.
The script `runApplication.sh` will prompt for the api key and pass it to the application via the environmental variable `API_KEY`.

I copied information from
[this web page](https://www.schemecolor.com/color-names-supported-on-all-web-browsers.php) to create the `colorToRgb.properties` file - see `RgbColorLookup`

I created the final response in 2 stages. 
1. get the valid products and map them to ProductInfo instances. See `ProductInfoService`.
The data from this stage could be cached to improved performance/reduce load on JL product catalog.
2. filter/sort the ProductInfo instances and then add the `priceLabel`. See `ReducedProductsService`.

## Assumptions

I've used Java's `Currency` class to decode the supplied currency code - if this fails - 'GBP' will be used as a default.
I've ignored Products without a current/now price greater than 0.

I ignored the 'now/from' data field. I've used the 'now/to' data field to represent the current price.

When the labelType is `ShowWasThenNow` - if there is no 'Then' price, it falls back to `ShowWasNow`

When there is no `color` or `rgbColor` - this program will output `null` in the response.

JL Catalog requests failed without a `User-Agent` request header. I used `SpringBootApp` - just to get it to work.

## Misc

There is a postman collection file `JLReducedPriceDresses.postman_collection.json` which shows how to make calls for every `labelType` with example responses.

I have added `@Disabled` to a few integration tests that were used for more 'ad hoc' testing.

## TODO

### Caching
It would make sense to cache some data to reduce the load on the JL catalog service.

### CI/CD
Use github actions to create Docker Image ?

### Reactive APIS
Use Spring's WebFlux and reactive WebClient ?

### Improved Logging
It might be useful to be able to see inbound request/response and outbound request/response when logging is set to DEBUG.

### Application Health Checks
These could be added via Spring Boot Actuator

### API Flexibility
This application allows you get reduced price dresses. Further improvements could be:
1. Allow other products other than dresses to be supported. 
2. Allow **all** dresses to be returned - not just those that are reduced in price.

### OpenAPI/Swagger
It would be nice to create/generate an OpenAPI spec to document this application's API and allow others to develop against it.
