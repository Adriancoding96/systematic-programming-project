# GrepAI

## Overview

The **GrepAI** serves as an intermediary layer between users and Large Language Model (LLM) providers like ChatGPT. It evaluates responses from LLMs for quality, captures key metadata (such as programming languages mentioned), and enables users to save, rank, and later search previously retrieved responses. If a highly-rated past response closely matches a new prompt, the system returns it from the database instead of making an LLM call.

## Features

- **Fuzzy Finding**: Efficiently streams databas tables and fuzzy finds prompts and related responses based on user input.  
- **Response Metadata Extraction**: Identifies programming languages or other relevant attributes in the response.  
- **User-Driven Moderation**: Users decide whether to save or discard a response. If saved, users can rank it as:
  - Slightly Useful
  - Useful
  - Very Useful
- **Searching & Reuse**: 
  - Users can search for past responses by criteria like programming language, quality ranking, or keywords.  
  - If a future prompt closely resembles a previous one and that previous response is highly ranked, the system can return it directly, bypassing the LLM.

## Technology Stack

**Java**, **Maven**, **Spring Boot**, **PostgreSQL**, **Docker**

## Usage

**Run API**

docker compose up --build

**Run test locally**

docker-compose up -d db

mvn clean test

## Endpoints

**Main Endpoints**

https://www.postman.com/adriannilsson96/workspace/my-workspace/collection/29968413-ded6d12f-2bbb-4d01-8e40-83ec837ed4cd?action=share&creator=29968413

**CRUD Endpoints**

https://www.postman.com/adriannilsson96/workspace/my-workspace/collection/29968413-fd4e0ed0-8288-4c9b-89d4-c7b8db92b5f8?action=share&creator=29968413

https://www.postman.com/adriannilsson96/workspace/my-workspace/collection/29968413-f261e883-067f-4aa6-b1f3-59e25c16fc65?action=share&creator=29968413

## Documentation
https://adriancoding96.github.io/systematic-programming-project/overview-tree.html
