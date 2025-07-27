# Task Board

A console-based task board application built with Java and JDBC for basic CRUD operations. This project was developed as part of the DIO GFT #7 Bootcamp curriculum.

## Features

- Create and manage multiple task boards
- Add custom columns to boards with ordered positioning
- Console-based interface with menu navigation
- Full CRUD operations (Create, Read, Update, Delete)
- MySQL database integration using JDBC

## Technologies Used

- **Java** - Core programming language
- **JDBC** - Database connectivity
- **MySQL** - Database management system

## Database Schema

The application uses the following main tables:
- `boards` - Stores board information
- `board_columns` - Manages board columns with ordering and categorization

## Getting Started

### Prerequisites

- Java 8 or higher
- MySQL database server
- MySQL JDBC driver

### Setup

1. Clone the repository
2. Set up your MySQL database
3. Configure database connection parameters in the application
4. Run the application

### Usage

The application provides a console menu with the following options:
1. Create a new board
2. Select an existing board
3. Delete a board
4. Exit

When creating a board, you can:
- Set a custom board name
- Add additional columns beyond the default ones
- Define column categories and ordering

## Project Structure

This is a study project focused on demonstrating:
- JDBC connectivity and database operations
- Console-based user interaction
- Object-oriented programming principles
- Database schema design with foreign keys and constraints

## Learning Objectives

- Understanding JDBC fundamentals
- Implementing CRUD operations
- Working with relational databases
- Console application development in Java