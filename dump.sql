-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Erstellungszeit: 13. Mrz 2021 um 11:47
-- Server-Version: 5.7.30
-- PHP-Version: 7.4.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `discordbot`
--
CREATE DATABASE IF NOT EXISTS `discordbot` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `discordbot`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `channel`
--

CREATE TABLE `channel` (
  `id_channel` varchar(100) NOT NULL,
  `channel_name` varchar(50) NOT NULL,
  `number_message` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
-- Tabellenstruktur für Tabelle `role`
--

CREATE TABLE `role` (
  `id_role` varchar(100) NOT NULL,
  `role_name` varchar(50) NOT NULL,
  `role_color` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user`
--

CREATE TABLE `user` (
  `id_discord` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `user_tag` varchar(50) NOT NULL,
  `avatar_url` varchar(500) NOT NULL,
  `status` varchar(50) NOT NULL
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

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_role`
--

CREATE TABLE `user_role` (
  `id_user_role` int(11) NOT NULL,
  `id_discord` varchar(100) NOT NULL,
  `id_role` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `channel`
--
ALTER TABLE `channel`
  ADD PRIMARY KEY (`id_channel`);

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
-- Indizes für die Tabelle `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id_role`);

--
-- Indizes für die Tabelle `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_discord`);

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
-- Indizes für die Tabelle `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`id_user_role`),
  ADD KEY `id_discord` (`id_discord`),
  ADD KEY `id_discord_2` (`id_discord`),
  ADD KEY `role_name` (`id_role`);

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
  MODIFY `id_user_bump_time` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT für Tabelle `user_join`
--
ALTER TABLE `user_join`
  MODIFY `id_user_join` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT für Tabelle `user_leave`
--
ALTER TABLE `user_leave`
  MODIFY `id_user_leave` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT für Tabelle `user_role`
--
ALTER TABLE `user_role`
  MODIFY `id_user_role` int(11) NOT NULL AUTO_INCREMENT;

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
  ADD CONSTRAINT `user_message_content_ibfk_1` FOREIGN KEY (`id_discord`) REFERENCES `user_message` (`id_discord`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `user_role`
--
ALTER TABLE `user_role`
  ADD CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`id_discord`) REFERENCES `user` (`id_discord`),
  ADD CONSTRAINT `user_role_ibfk_2` FOREIGN KEY (`id_role`) REFERENCES `role` (`id_role`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;