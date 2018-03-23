package com.cinema.http.model

case class Authenticate(
  username: String,
  password: String
)

case class AuthenticateResponse(
  user: User,
  token: String
)
