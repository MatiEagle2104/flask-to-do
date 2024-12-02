"use strict";

const express = require("express");
const path = require("path");
const mssql = require("mssql");
const log4js = require("log4js"); // Dodajemy log4js
const dbConfig = require("./config/dbConfig");

const app = express();

// Konfiguracja log4js
log4js.configure({
    appenders: { file: { type: "file", filename: "log4js.log" } },
    categories: { default: { appenders: ["file"], level: "error" } }
});
const logger = log4js.getLogger();

// Ustawienia Express
app.use(express.static(path.join(__dirname, "public")));
app.use(express.json());

app.post("/movies", async (req, res) => {
    try {
        var connection = await mssql.connect(dbConfig);
        const result = await mssql.query(`SELECT * FROM Movie WHERE Title LIKE '%${req.body.title}%'`);

        if (result.recordset.length === 0) {
            logger.error("No movies found for title: " + req.body.title);  // Logowanie błędu
            res.status(404).json({
                message: "No Movie Found"
            });
        } else {
            res.status(200).json(result.recordset);
        }
    } catch (error) {
        logger.error("Database error: " + error.message);  // Logowanie błędu bazy danych
        res.status(500).json({
            message: "Internal Server Error",
        });
    } finally {
        if (connection) {
            connection.close();
        } else {
            res.status(500).json({
                message: "Internal Server Error",
            });
        }
    }
});

app.use((req, res) => {
    res.sendFile(path.join(__dirname, "public", "404.html"));
});

const port = process.env.PORT || 3000;
app.listen(port, () => {
    console.log(`Listening on port 3000`);
    logger.info("Server started on port " + port);  // Logowanie informacji o starcie serwera
});
