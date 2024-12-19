# Chatbot Middleware API

## Overview

The **Chatbot Middleware API** serves as an intermediary layer between users and Large Language Model (LLM) providers like ChatGPT. It evaluates responses from LLMs for quality, captures key metadata (such as programming languages mentioned), and enables users to save, rank, and later search previously retrieved responses. If a highly-rated past response closely matches a new prompt, the system returns it from the database instead of making an LLM call.

## Features

- **Quality Evaluation**: Automatically checks LLM responses for issues (e.g., bad formatting, irrelevant content).  
- **Response Metadata Extraction**: Identifies programming languages or other relevant attributes in the response.  
- **User-Driven Moderation**: Users decide whether to save or discard a response. If saved, users can rank it as:
  - Slightly Useful
  - Useful
  - Very Useful
- **Searching & Reuse**: 
  - Users can search for past responses by criteria like programming language, quality ranking, or keywords.  
  - If a future prompt closely resembles a previous one and that previous response is highly ranked, the system can return it directly, bypassing the LLM.

## Technology Stack

- **Java**
- **Maven**
- **Spring Boot**
- **MySQL**
- **Hibernate**
- **JPA**
- **Lombok**
- **Log4J**
- **JUnit**
- **Mockito**
- **Git**
- **Docker**
- **E3 (AWS)**
