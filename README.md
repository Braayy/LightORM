#LightORM
_A lightweight and performant Java ORM alternative._

---

![Java version](https://img.shields.io/static/v1?label=Java&message=1.8&color=Orange&style=for-the-badge)
[![GitHub stars](https://img.shields.io/github/stars/JPereirax/LightORM.svg?style=for-the-badge&color=orange&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAADYklEQVR4XuWbvbPNQBiHn9uo6NFT0GEYHa0/QofOR2MojI8xxmh8VFwVf4RaxTB0KOhdPRUF8zPJmdy1J3l3N9ndxDb3zr0n2exzfu/zJjk5G5Qdh5vp35c6jI1SEzfzbgK/gXOljqMkgF3A1wbAXuBHCQglAehdf9wsWr8rDdlHSQCq+0MdBxzJvnqgFADJ752zYAHILsNSABT3Mw4A/S27DEsAaOW30wHwHcguwxIAuvJzyz67DEsA6MrPBaD/ZZVhbgA++bkQssowNwCf/FwAWWWYE8A6+bkAssowJ4A++RWTYU4AffIrJsNcACzyKyLDXAAs8isiwxwArPIrIsMcAELkl12GOQCEyC+7DAUgpj5LXLpPMeemAOwAXgLHp5ih4n2+Ak62JbCnuRmhn//D2ALUmre6DlAClAQlYsnjJ3ACeK1FuhI8CzxZ8uqbu06rG7C+LrBkKf5zpekDsFQp/pUeoBJYjXXnAUuT4kp6bnn3nQgtRYrbpBcCQK9dghR7b7RaToXnLMXB22sWAHOVold6oSXQvn5uUlwrvVgA2m4uUuyVXgqAuUgx6NMliwN8t6rcDzZrOXselF5qArR9rVI0SW8MANpHbVI0S28sADVJMUh6YwKoRYpB0hsbwEHgQ2ED6hg+xR5DTBfoznUDuB47+Ujb6Rhuxu4rFcBH4EDs5CNtp3dfKYgaKQBqiH+76OgySAFQQ/xbANFlkAKghvi3AKLLIBZATfFPKoNYADXFP6kMYgHUFP+kMogBUGP8o8sgBsBY8f8MXGqO/D6wP6qRb98ouBvEAEiNvx6DuwU8BH41x69L7AvANUBPlMSO4G4QCiAl/vpqzDPgKvBtzQp3A3eB0wmP8gedFIUCiI3/G+A88Nb41h4DHgFHja93r0/M1wahAELjr3f6CvC8+W5QyHp0bEqCEqFkWEdQGYQACIm/blI8AG4DqvmUISfIDXKE9dkFcxmEALDG/wVwEfiSsmrPtvsaqKcM+zV3gxAAQ/Fv25oATDkEYKhtmsvACqAv/r62NiUA7dvSNk1lYAXgi7+lrU0Noq9tmsrACsCNf2hbmxqEr22aysACoBv/lLY2NQRf2xwsAwsARUlnb2O1talBdNvmnaEbphYA94CnE7S1qUGobeozzMt9E/0BUX274HXoySkAAAAASUVORK5CYII=)](https://github.com/JPereirax/LightORM/stargazers)
[![Github Issues](https://img.shields.io/github/issues-raw/JPereirax/LightORM.svg?style=for-the-badge&color=orange&logo=data:image/png;base64,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)](https://github.com/JPereirax/LightORM/issues)
[![MIT License](https://img.shields.io/badge/license-MIT-blue.svg?color=ORANGE&style=for-the-badge)](https://choosealicense.com/licenses/mit)

LightORM has annotation processors so that all the integration code with the database is generated at the time of compilation, thus ensuring that you will not have any performance problems.

## Getting Started

### Setup dependencies

---

- Maven
```xml
<dependency>
    <groupId>com.jpereirax</groupId>
    <artifactId>lightorm</artifactId>
    <version>0.1.0</version>
</dependency>
```

- Gradle
```groovy
dependencies {
    implementation 'com.jpereirax:lightorm:0.1.0'
    annotationProcessor 'com.jpereirax:lightorm:0.1.0'
}
```

##Samples

User
```java
public class User {
    
    private String name;
    private Integer level;
    private String rank;
    
    // Needed to map the query result to the object.
    public User(String name, Integer level, String rank) {
        this.name = name;
        this.level = level;
        this.rank = rank;
    }
    
    // Other methods...
}
```

DataProvider
```java
@DataProvider
public interface userDataProvider {
    
    @Query("SELECT NAME, LEVEL, RANK FROM USER WHERE UUID = ?")
    User findByUUID(String uuid);
}
```
