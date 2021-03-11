-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Erstellungszeit: 10. Mrz 2021 um 07:24
-- Server-Version: 5.7.30
-- PHP-Version: 7.4.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";
 
 
   -- Datenbank: `discordbot`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `deleted_message`
--

CREATE TABLE `deleted_message` (
  `id` int(11) NOT NULL,
  `id_discord` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `content` varchar(2500) NOT NULL,
  `deleted_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ping_data`
--

CREATE TABLE `ping_data` (
  `id_user` int(11) NOT NULL,
  `id_author` varchar(100) NOT NULL,
  `id_pinged_user` varchar(100) NOT NULL,
  `author_username` varchar(50) NOT NULL,
  `pinged_user_username` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_bump`
--

CREATE TABLE `user_bump` (
  `id_discord` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `number_bumps` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Daten für Tabelle `user_bump`
   
   
--

INSERT INTO `user_bump` (`id_discord`, `username`, `number_bumps`) VALUES
('3215', 'rar', 54456),
('334246', 'ehr', 2),
('4563457', 'egrwhrw', 9),
('739143338975952959', 'Lem', 1),
('r43264', 'errhrw', 3);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_bump_time`
--

CREATE TABLE `user_bump_time` (
  `id_user_bump_time` int(11) NOT NULL,
  `id_discord` varchar(100) NOT NULL,
  `bumped_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_join`
--

CREATE TABLE `user_join` (
  `id_user_join` int(11) NOT NULL,
  `id_discord` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `avatar_url` varchar(500) DEFAULT NULL,
  `joined_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
  
  -- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_leave`
--

CREATE TABLE `user_leave` (
  `id_user_leave` int(11) NOT NULL,
  `id_discord` varchar(100) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `avatar_url` varchar(500) DEFAULT NULL,
  `user_tag` varchar(50) NOT NULL,
  `left_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_message`
--

CREATE TABLE `user_message` (
  `id_discord` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `number_message` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
 
 -- Tabellenstruktur für Tabelle `user_message_content`
--

CREATE TABLE `user_message_content` (
  `id_message` varchar(100) NOT NULL,
  `id_discord` varchar(100) NOT NULL,
  `content` varchar(2500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `postet_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `deleted_message`
--
ALTER TABLE `deleted_message`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `ping_data`
--
ALTER TABLE `ping_data`
  ADD PRIMARY KEY (`id_user`);

--
-- Indizes für die Tabelle `user_bump`
--
ALTER TABLE `user_bump`
  ADD PRIMARY KEY (`id_discord`);

--
 -- Indizes für die Tabelle `user_bump_time`
--
ALTER TABLE `user_bump_time`
  ADD PRIMARY KEY (`id_user_bump_time`),
  ADD KEY `id_discord` (`id_discord`);

--
-- Indizes für die Tabelle `user_join`
--
ALTER TABLE `user_join`
  ADD PRIMARY KEY (`id_user_join`);

--
-- Indizes für die Tabelle `user_leave`
--
ALTER TABLE `user_leave`
  ADD PRIMARY KEY (`id_user_leave`);

--
-- Indizes für die Tabelle `user_message`
--
ALTER TABLE `user_message`
  ADD PRIMARY KEY (`id_discord`);

--
-- Indizes für die Tabelle `user_message_content`
--
ALTER TABLE `user_message_content`
  ADD PRIMARY KEY (`id_message`),
  ADD KEY `id_discord` (`id_discord`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `deleted_message`
--
 
 
 ALTER TABLE `deleted_message`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `ping_data`
--
ALTER TABLE `ping_data`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `user_bump_time`
--
ALTER TABLE `user_bump_time`
  MODIFY `id_user_bump_time` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT für Tabelle `user_join`
--
ALTER TABLE `user_join`
  MODIFY `id_user_join` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT für Tabelle `user_leave`
--
ALTER TABLE `user_leave`
  MODIFY `id_user_leave` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `user_bump_time`
--
ALTER TABLE `user_bump_time`
  ADD CONSTRAINT `user_bump_time_ibfk_1` FOREIGN KEY (`id_discord`) REFERENCES `user_bump` (`id_discord`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `user_message_content`
--
ALTER TABLE `user_message_content`
  ADD CONSTRAINT `user_message_content_ibfk_1` FOREIGN KEY (`id_discord`) REFERENCES `user_message` (`id_discord`);