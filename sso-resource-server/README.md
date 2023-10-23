# Resource Server

## 授权模式

### 授权码模式

访问 /oauth/authorize 端点

response_type: code，在重定向地址后返回授权码code，再根据授权码获取access_token

```http request
GET http://localhost:8888/oauth/authorize?client_id=ops&response_type=code&state=123456&scope=read_userinfo read_contacts&redirect_uri=http://localhost:9080/ops/callback
```

接下来访问 /oauth/token 端点，换取access_token

```http request
POST http://ops:ops_secret@localhost:8888/oauth/token?grant_type=authorization_code&code=授权码&redirect_uri=http://localhost:9080/ops/callback
```

### 简化模式

访问 /oauth/authorize 端点

response_type: token 直接在重定向地址后返回access_token

```http request
GET http://localhost:8888/oauth/authorize?client_id=ops&response_type=token&state=123456&scope=read_userinfo read_contacts&redirect_uri=http://localhost:9080/ops/callback
```
认证成功后，浏览器会重定向到 http://localhost:9080/ops/callback#access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTgzODg4MDksInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiJhZWQ4MDQzMi1jNGU0LTRiNGMtOTg5YS1kOTc2M2Q4ZDY4NjAiLCJjbGllbnRfaWQiOiJvcHMiLCJzY29wZSI6WyJyZWFkX2NvbnRhY3RzIl19.2BqS1RNKlwc0m5JAln_QdOHQoZG2jgzRnCaU1zV9sMU&token_type=bearer&state=123456&expires_in=7199&scope=read_contacts&jti=aed80432-c4e4-4b4c-989a-d9763d8d6860

### 密码模式

访问 /oauth/token 端点

grant_type: password，直接返回access_token

```http request
POST http://ops:ops_secret@localhost:8888/oauth/token?grant_type=password&username=admin&password=123456&scope=read_userinfo read_contacts
```

### 客户端模式

访问 /oauth/token 端点

grant_type: client_credentials，直接返回access_token

```http request
POST http://ops:ops_secret@localhost:8888/oauth/token?grant_type=client_credentials&scope=read_userinfo read_contacts
```

### 刷新token

首先访问授权码或密码模式获取access_token，然后访问该端点，用refresh_token换取access_token

访问 /oauth/token 端点

grant_type: refresh_token，直接返回access_token

```http request
POST http://ops:ops_secret@localhost:8888/oauth/token?grant_type=refresh_token&scope=read_userinfo read_contacts&refresh_token=刷新token
```

## 访问受保护的资源

访问 /ops/userinfo 资源

```http request
GET http://localhost:9080/ops/userinfo
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTgzODkyNTksInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiI4MTVjMjZkZi04MTEyLTQ0NDItOTA1ZS0zMDk2MTI3NWUwMDEiLCJjbGllbnRfaWQiOiJvcHMiLCJzY29wZSI6WyJyZWFkX2NvbnRhY3RzIiwicmVhZF91c2VyaW5mbyJdfQ.uHZ5xYKP9hwl2wOCEGWpNH060QCIxQb2ToMaZ4zEoZY
```