# slimstore-backend

## Micro Service

This repository is part of a wider set of repositories that form a set of micro services that complete the whole application.

 - https://github.com/madlou/slimstore-infrastructure
 - https://github.com/madlou/slimstore-backend
 - https://github.com/madlou/slimstore-frontend
 - https://github.com/madlou/slimstore-customerdisplay

The application is currently deployed on the Oracle platform and can be found here:

 - https://slimstore.matthews.cloud/ 

  
## Local Development

You can run a local dev environment with:

```nix develop```
```mvn spring-boot:run```

## Class Dependancy Tree

```
basket      -> form
display     -> basket, register, store, tender, transaction, translation
form       <-> view
giftcard    -> form, register
pos         -> everything
print       -> basket, register, tender
product     -> form
register    -> form, store, translation
store       -> form, translation
tender      -> basket, form, translation
transaction -> basket, form, register, store, tender, user
translation -> form, view
user        -> form, register, store, translation, view
```