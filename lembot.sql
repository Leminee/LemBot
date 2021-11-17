-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Erstellungszeit: 17. Nov 2021 um 23:07
-- Server-Version: 10.3.31-MariaDB-0+deb10u1
-- PHP-Version: 7.4.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `lembot`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `active_user`
--

CREATE TABLE `active_user` (
  `id_active_user` bigint(20) NOT NULL,
  `active_member` int(11) NOT NULL,
  `time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `banned_user`
--

CREATE TABLE `banned_user` (
  `id_banned_user` bigint(20) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `ban_author` varchar(255) NOT NULL,
  `ban_reason` text NOT NULL,
  `channel_name` varchar(255) NOT NULL,
  `banned_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `channel`
--

CREATE TABLE `channel` (
  `id_message` bigint(20) NOT NULL,
  `id_channel` bigint(20) NOT NULL,
  `channel_name` varchar(255) NOT NULL,
  `sent_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `config`
--

CREATE TABLE `config` (
  `config_name` varchar(50) NOT NULL,
  `config_value` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `deleted_message`
--

CREATE TABLE `deleted_message` (
  `id_deleted_message` bigint(20) NOT NULL,
  `deleted_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `kicked_user`
--

CREATE TABLE `kicked_user` (
  `id_kicked_user` int(11) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `kick_author` varchar(50) NOT NULL,
  `kick_reason` varchar(50) NOT NULL,
  `channel_name` varchar(50) NOT NULL,
  `kicked_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `leaked_password`
--

CREATE TABLE `leaked_password` (
  `id_pass` bigint(20) NOT NULL,
  `pass` varchar(500) NOT NULL,
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `muted_user`
--

CREATE TABLE `muted_user` (
  `id_muted_user` int(11) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `mute_author` varchar(255) NOT NULL,
  `mute_reason` text NOT NULL,
  `channel_name` varchar(255) NOT NULL,
  `activ` tinyint(4) NOT NULL DEFAULT 1,
  `muted_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `number_member`
--

CREATE TABLE `number_member` (
  `id_number_member` bigint(20) NOT NULL,
  `total_member` smallint(6) NOT NULL,
  `time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `updated_message`
--

CREATE TABLE `updated_message` (
  `id_updated_message` bigint(11) NOT NULL,
  `id_message` bigint(20) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `username` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `updated_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `updated_username`
--

CREATE TABLE `updated_username` (
  `id_updated_username` bigint(20) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) NOT NULL,
  `old_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `new_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `updated_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_bump`
--

CREATE TABLE `user_bump` (
  `id_discord` bigint(20) NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `number_bumps` mediumint(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_bump_time`
--

CREATE TABLE `user_bump_time` (
  `id_user_bump_time` bigint(11) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `bumped_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_join`
--

CREATE TABLE `user_join` (
  `id_user_join` bigint(20) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `avatar_url` text DEFAULT NULL,
  `joined_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_leave`
--

CREATE TABLE `user_leave` (
  `id_user_leave` bigint(20) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `avatar_url` text DEFAULT NULL,
  `left_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_message`
--

CREATE TABLE `user_message` (
  `id_discord` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `number_message` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_message_content`
--

CREATE TABLE `user_message_content` (
  `id_message` bigint(20) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `posted_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_status`
--

CREATE TABLE `user_status` (
  `id_user_status` bigint(11) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `changed_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `voice_join`
--

CREATE TABLE `voice_join` (
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `voice_channel_name` varchar(255) NOT NULL,
  `joined_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `voice_leave`
--

CREATE TABLE `voice_leave` (
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `voice_channel_name` varchar(255) NOT NULL,
  `left_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `warned_user`
--

CREATE TABLE `warned_user` (
  `id_warned_user` bigint(11) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `warn_author` varchar(255) NOT NULL,
  `warn_reason` text NOT NULL,
  `channel_name` varchar(255) NOT NULL,
  `warned_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `active_user`
--
ALTER TABLE `active_user`
  ADD PRIMARY KEY (`id_active_user`),
  ADD KEY `active_member` (`active_member`);

--
-- Indizes für die Tabelle `banned_user`
--
ALTER TABLE `banned_user`
  ADD PRIMARY KEY (`id_banned_user`);

--
-- Indizes für die Tabelle `channel`
--
ALTER TABLE `channel`
  ADD PRIMARY KEY (`id_message`);

--
-- Indizes für die Tabelle `config`
--
ALTER TABLE `config`
  ADD PRIMARY KEY (`config_name`);

--
-- Indizes für die Tabelle `deleted_message`
--
ALTER TABLE `deleted_message`
  ADD PRIMARY KEY (`id_deleted_message`);

--
-- Indizes für die Tabelle `kicked_user`
--
ALTER TABLE `kicked_user`
  ADD PRIMARY KEY (`id_kicked_user`);

--
-- Indizes für die Tabelle `leaked_password`
--
ALTER TABLE `leaked_password`
  ADD PRIMARY KEY (`id_pass`),
  ADD KEY `pass` (`pass`);

--
-- Indizes für die Tabelle `muted_user`
--
ALTER TABLE `muted_user`
  ADD PRIMARY KEY (`id_muted_user`);

--
-- Indizes für die Tabelle `number_member`
--
ALTER TABLE `number_member`
  ADD PRIMARY KEY (`id_number_member`),
  ADD KEY `total_member` (`total_member`);

--
-- Indizes für die Tabelle `updated_message`
--
ALTER TABLE `updated_message`
  ADD PRIMARY KEY (`id_updated_message`),
  ADD KEY `id_message` (`id_message`);

--
-- Indizes für die Tabelle `updated_username`
--
ALTER TABLE `updated_username`
  ADD PRIMARY KEY (`id_updated_username`);

--
-- Indizes für die Tabelle `user_bump`
--
ALTER TABLE `user_bump`
  ADD PRIMARY KEY (`id_discord`),
  ADD KEY `number_bumps` (`number_bumps`);

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
  ADD PRIMARY KEY (`id_discord`),
  ADD KEY `number_message` (`number_message`);

--
-- Indizes für die Tabelle `user_message_content`
--
ALTER TABLE `user_message_content`
  ADD PRIMARY KEY (`id_message`),
  ADD KEY `id_discord` (`id_discord`);

--
-- Indizes für die Tabelle `user_status`
--
ALTER TABLE `user_status`
  ADD PRIMARY KEY (`id_user_status`);

--
-- Indizes für die Tabelle `warned_user`
--
ALTER TABLE `warned_user`
  ADD PRIMARY KEY (`id_warned_user`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `active_user`
--
ALTER TABLE `active_user`
  MODIFY `id_active_user` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `banned_user`
--
ALTER TABLE `banned_user`
  MODIFY `id_banned_user` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `kicked_user`
--
ALTER TABLE `kicked_user`
  MODIFY `id_kicked_user` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `leaked_password`
--
ALTER TABLE `leaked_password`
  MODIFY `id_pass` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `muted_user`
--
ALTER TABLE `muted_user`
  MODIFY `id_muted_user` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `number_member`
--
ALTER TABLE `number_member`
  MODIFY `id_number_member` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `updated_message`
--
ALTER TABLE `updated_message`
  MODIFY `id_updated_message` bigint(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `updated_username`
--
ALTER TABLE `updated_username`
  MODIFY `id_updated_username` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `user_bump_time`
--
ALTER TABLE `user_bump_time`
  MODIFY `id_user_bump_time` bigint(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `user_join`
--
ALTER TABLE `user_join`
  MODIFY `id_user_join` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `user_leave`
--
ALTER TABLE `user_leave`
  MODIFY `id_user_leave` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `user_status`
--
ALTER TABLE `user_status`
  MODIFY `id_user_status` bigint(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `warned_user`
--
ALTER TABLE `warned_user`
  MODIFY `id_warned_user` bigint(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `deleted_message`
--
ALTER TABLE `deleted_message`
  ADD CONSTRAINT `deleted_message_ibfk_2` FOREIGN KEY (`id_deleted_message`) REFERENCES `user_message_content` (`id_message`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `updated_message`
--
ALTER TABLE `updated_message`
  ADD CONSTRAINT `updated_message_ibfk_1` FOREIGN KEY (`id_message`) REFERENCES `user_message_content` (`id_message`);

--
-- Constraints der Tabelle `user_bump_time`
--
ALTER TABLE `user_bump_time`
  ADD CONSTRAINT `user_bump_time_ibfk_1` FOREIGN KEY (`id_discord`) REFERENCES `user_bump` (`id_discord`);

--
-- Constraints der Tabelle `user_message_content`
--
ALTER TABLE `user_message_content`
  ADD CONSTRAINT `user_message_content_ibfk_1` FOREIGN KEY (`id_discord`) REFERENCES `user_message` (`id_discord`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
