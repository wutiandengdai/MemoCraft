#!/bin/bash

# MongoDB setup
echo "Setting up MongoDB..."
mongo <<EOF
use memo_craft_text
db.createCollection("textDocuments")
EOF
echo "MongoDB setup complete."

# PostgreSQL setup
echo "Setting up PostgreSQL..."
psql -U memo_craft <<EOF
CREATE DATABASE memo_craft;
\c memo_craft

CREATE TYPE note_tag AS ENUM ('BUSINESS', 'PERSONAL', 'IMPORTANT');
CREATE TABLE notes (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    text TEXT NOT NULL,
    tags note_tag[]
);
EOF
echo "PostgreSQL setup complete."
