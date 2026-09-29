
<h1 align="center">Wusool (وصول)</h1>

<p align="center">
  A Spring Boot REST API that connects people to <b>accessible</b> places — search, verify, and book, all in one flow.
</p>

---

## Overview

Wusool lets business owners register places with structured **accessibility details** (ramps, elevators, accessible restrooms, parking, etc.), lets users **search and filter** by real accessibility features and book a visit in one step, and adds an **AI assistant** (Gemini) for free‑text accessibility questions plus automatic **email/WhatsApp** booking notifications.

**Stack:** Spring Boot · Spring Data JPA / Hibernate · MySQL · Jakarta Validation · Gemini API · Spring Mail · UltraMsg (WhatsApp)

Base URL (local): `http://localhost:8080`

---

## Table of Contents

- [Users](#users--apiv1user)
- [Admins](#admins--apiv1admin)
- [Business Owners](#business-owners--apiv1business-owner)
- [Places](#places--apiv1place)
- [Place Categories](#place-categories--apiv1place-category)
- [Place Accessibility](#place-accessibility--apiv1place-accessibility)
- [Place Images](#place-images--apiv1place-image)
- [Bookings](#bookings--apiv1booking)
- [Reviews](#reviews--apiv1review)
- [AI Assistant](#ai-assistant--ai)

---

## Users — `/api/v1/user`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/user/get` | Get all users |
| POST | `/api/v1/user/add` | Add a new user |
| PUT | `/api/v1/user/update/{id}` | Update a user by id |
| DELETE | `/api/v1/user/delete/{id}` | Delete a user by id |

## Admins — `/api/v1/admin`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/admin/get` | Get all admins |
| POST | `/api/v1/admin/add` | Add a new admin |
| PUT | `/api/v1/admin/update/{id}` | Update an admin by id |
| DELETE | `/api/v1/admin/delete/{id}` | Delete an admin by id |

## Business Owners — `/api/v1/business-owner`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/business-owner/get` | Get all business owners |
| POST | `/api/v1/business-owner/add` | Add a new business owner |
| PUT | `/api/v1/business-owner/update/{id}` | Update a business owner by id |
| DELETE | `/api/v1/business-owner/delete/{id}` | Delete a business owner (fails if the owner still has places) |

## Places — `/api/v1/place`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/place/get` | Get all places |
| POST | `/api/v1/place/add` | Add a new place |
| PUT | `/api/v1/place/update/{id}` | Update a place by id |
| DELETE | `/api/v1/place/delete/{id}` | Delete a place (fails if it has bookings) |
| GET | `/api/v1/place/visited-by/{userId}` | Places a given user has visited |
| GET | `/api/v1/place/city/{city}` | Places located in a given city |
| GET | `/api/v1/place/category/{categoryId}` | Places under a given category |
| GET | `/api/v1/place/accessibility/{type}` | Places that support a given accessibility type |
| GET | `/api/v1/place/open-now` | Places currently open |
| GET | `/api/v1/place/available/{time}` | Places open at a specific `LocalTime` — **JPQL** |
| GET | `/api/v1/place/most-visited` | Places ranked by number of completed bookings — **JPQL** |
| GET | `/api/v1/place/search/{city}/{category}` | Places in a city matching a (partial) category name — **JPQL** |

## Place Categories — `/api/v1/place-category`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/place-category/get` | Get all place categories |
| POST | `/api/v1/place-category/add/{adminId}` | Add a category (requires an admin id) |
| PUT | `/api/v1/place-category/update/{adminId}/{id}` | Update a category (requires an admin id) |
| DELETE | `/api/v1/place-category/delete/{adminId}/{id}` | Delete a category (requires an admin id) |

## Place Accessibility — `/api/v1/place-accessibility`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/place-accessibility/get` | Get all place accessibility records |
| POST | `/api/v1/place-accessibility/add` | Add an accessibility record to a place |
| PUT | `/api/v1/place-accessibility/update/{id}` | Update an accessibility record |
| DELETE | `/api/v1/place-accessibility/delete/{id}` | Delete an accessibility record |

## Place Images — `/api/v1/place-image`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/place-image/get` | Get all place images |
| POST | `/api/v1/place-image/add` | Add an image to a place |
| PUT | `/api/v1/place-image/update/{id}` | Update a place image |
| DELETE | `/api/v1/place-image/delete/{id}` | Delete a place image |

## Bookings — `/api/v1/booking`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/booking/get` | Get all bookings |
| POST | `/api/v1/booking/add` | Add a booking (validates user, place, working hours, and slot availability) — uses **JPQL** internally |
| PUT | `/api/v1/booking/update/{id}` | Update a booking (re-validates the slot, excluding itself) — uses **JPQL** internally |
| DELETE | `/api/v1/booking/delete/{id}` | Delete a booking (fails if it already has a review) |
| GET | `/api/v1/booking/user-history/{userId}` | A user's booking history, newest first — **JPQL** |
| PUT | `/api/v1/booking/complete/{ownerId}/{bookingId}` | Owner marks a `PENDING` booking as `COMPLETED` |
| GET | `/api/v1/booking/place/{placeId}/{date}` | Bookings for a place on a specific date |
| POST | `/api/v1/booking/{bookingId}/send-email` | Send a booking confirmation email |

## Reviews — `/api/v1/review`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/review/get` | Get all reviews |
| POST | `/api/v1/review/add` | Add a review |
| PUT | `/api/v1/review/update/{id}` | Update a review (id, booking, and user must match the original) |
| DELETE | `/api/v1/review/delete/{id}` | Delete a review |
| GET | `/api/v1/review/place/{placeId}` | All reviews for a place |
| GET | `/api/v1/review/place/{placeId}/average` | Average rating of a place |


## AI Assistant — `/ai`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/ai/ask-ai` | Ask Gemini a free-text accessibility question about a place |


---

## Notifications

| Feature | Powered by |
|---|---|
| Booking confirmation emails | `EmailService` via Spring Mail (Gmail SMTP) |
| WhatsApp booking alerts | `WhatsappService` via the UltraMsg API |
| Accessibility Q&A | `GeminiService` via the Gemini API |

---


This project was built as a university capstone project.
