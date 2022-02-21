-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Erstellungszeit: 21. Feb 2022 um 06:53
-- Server-Version: 10.3.31-MariaDB-0+deb10u1
-- PHP-Version: 7.4.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `discordbot`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `advertising`
--

CREATE TABLE `advertising` (
  `id` int(11) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sent_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `banned_user`
--

CREATE TABLE `banned_user` (
  `id` bigint(20) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `ban_author` varchar(255) NOT NULL,
  `reason` text NOT NULL,
  `channel_name` varchar(255) NOT NULL,
  `banned_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `channel`
--

CREATE TABLE `channel` (
  `id_message` bigint(20) NOT NULL,
  `id_channel` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `sent_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `contributor`
--

CREATE TABLE `contributor` (
  `id` int(11) NOT NULL,
  `mention` varchar(255) NOT NULL,
  `contributor_since` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
-- Tabellenstruktur für Tabelle `exception`
--

CREATE TABLE `exception` (
  `id_exception` bigint(20) NOT NULL,
  `occurred_in` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `details` text NOT NULL,
  `occurred_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `invite_tracking`
--

CREATE TABLE `invite_tracking` (
  `id` int(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  `used_by` varchar(255) NOT NULL,
  `invited_by` varchar(255) NOT NULL,
  `amount` int(11) NOT NULL,
  `used_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `muted_user`
--

CREATE TABLE `muted_user` (
  `id` int(11) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `mute_author` varchar(255) NOT NULL,
  `reason` text NOT NULL,
  `duration` varchar(255) NOT NULL,
  `channel_name` varchar(255) NOT NULL,
  `activ` tinyint(4) NOT NULL DEFAULT 1,
  `muted_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `number_member`
--

CREATE TABLE `number_member` (
  `id` bigint(20) NOT NULL,
  `total_member` smallint(6) NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `staff`
--

CREATE TABLE `staff` (
  `id` int(11) NOT NULL,
  `mention` varchar(255) NOT NULL,
  `role_name` enum('Administrator','Moderator','') NOT NULL,
  `staff_since` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `updated_message`
--

CREATE TABLE `updated_message` (
  `id` bigint(11) NOT NULL,
  `id_message` bigint(20) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `username` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `updated_nickname`
--

CREATE TABLE `updated_nickname` (
  `id` bigint(20) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `old_nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `new_nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `updated_username`
--

CREATE TABLE `updated_username` (
  `id` int(11) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `old_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `new_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_attachment`
--

CREATE TABLE `user_attachment` (
  `id_attachment` bigint(20) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `url` text NOT NULL,
  `extension` varchar(255) NOT NULL,
  `size` decimal(20,0) NOT NULL,
  `uploaded_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
  `bumped_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_join`
--

CREATE TABLE `user_join` (
  `id` bigint(20) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `avatar_url` text DEFAULT NULL,
  `joined_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_leave`
--

CREATE TABLE `user_leave` (
  `id` bigint(20) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `avatar_url` text DEFAULT NULL,
  `left_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_message`
--

CREATE TABLE `user_message` (
  `id_discord` bigint(20) NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
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
  `posted_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_online`
--

CREATE TABLE `user_online` (
  `id` bigint(20) NOT NULL,
  `amount` int(11) NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_status`
--

CREATE TABLE `user_status` (
  `id` bigint(11) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `changed_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `voice_join`
--

CREATE TABLE `voice_join` (
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `joined_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `voice_leave`
--

CREATE TABLE `voice_leave` (
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `left_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `voice_move`
--

CREATE TABLE `voice_move` (
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `moved_from` varchar(255) NOT NULL,
  `moved_in_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `warned_user`
--

CREATE TABLE `warned_user` (
  `id` bigint(11) NOT NULL,
  `id_discord` bigint(20) NOT NULL,
  `user_tag` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `warn_author` varchar(255) NOT NULL,
  `reason` text NOT NULL,
  `channel_name` varchar(255) NOT NULL,
  `warned_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `advertising`
--
ALTER TABLE `advertising`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `banned_user`
--
ALTER TABLE `banned_user`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `channel`
--
ALTER TABLE `channel`
  ADD PRIMARY KEY (`id_message`);

--
-- Indizes für die Tabelle `contributor`
--
ALTER TABLE `contributor`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `deleted_message`
--
ALTER TABLE `deleted_message`
  ADD PRIMARY KEY (`id_deleted_message`);

--
-- Indizes für die Tabelle `exception`
--
ALTER TABLE `exception`
  ADD PRIMARY KEY (`id_exception`);

--
-- Indizes für die Tabelle `invite_tracking`
--
ALTER TABLE `invite_tracking`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `muted_user`
--
ALTER TABLE `muted_user`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `number_member`
--
ALTER TABLE `number_member`
  ADD PRIMARY KEY (`id`),
  ADD KEY `total_member` (`total_member`);

--
-- Indizes für die Tabelle `staff`
--
ALTER TABLE `staff`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `updated_message`
--
ALTER TABLE `updated_message`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_message` (`id_message`);

--
-- Indizes für die Tabelle `updated_nickname`
--
ALTER TABLE `updated_nickname`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `updated_username`
--
ALTER TABLE `updated_username`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `user_attachment`
--
ALTER TABLE `user_attachment`
  ADD KEY `id_discord` (`id_discord`);

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
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `user_leave`
--
ALTER TABLE `user_leave`
  ADD PRIMARY KEY (`id`);

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
-- Indizes für die Tabelle `user_online`
--
ALTER TABLE `user_online`
  ADD PRIMARY KEY (`id`),
  ADD KEY `active_member` (`amount`);

--
-- Indizes für die Tabelle `user_status`
--
ALTER TABLE `user_status`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `warned_user`
--
ALTER TABLE `warned_user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `advertising`
--
ALTER TABLE `advertising`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `banned_user`
--
ALTER TABLE `banned_user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `contributor`
--
ALTER TABLE `contributor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `exception`
--
ALTER TABLE `exception`
  MODIFY `id_exception` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `invite_tracking`
--
ALTER TABLE `invite_tracking`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `muted_user`
--
ALTER TABLE `muted_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `number_member`
--
ALTER TABLE `number_member`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `staff`
--
ALTER TABLE `staff`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `updated_message`
--
ALTER TABLE `updated_message`
  MODIFY `id` bigint(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `updated_nickname`
--
ALTER TABLE `updated_nickname`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `updated_username`
--
ALTER TABLE `updated_username`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `user_bump_time`
--
ALTER TABLE `user_bump_time`
  MODIFY `id_user_bump_time` bigint(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `user_join`
--
ALTER TABLE `user_join`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `user_leave`
--
ALTER TABLE `user_leave`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `user_online`
--
ALTER TABLE `user_online`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `user_status`
--
ALTER TABLE `user_status`
  MODIFY `id` bigint(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `warned_user`
--
ALTER TABLE `warned_user`
  MODIFY `id` bigint(11) NOT NULL AUTO_INCREMENT;

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
-- Constraints der Tabelle `user_attachment`
--
ALTER TABLE `user_attachment`
  ADD CONSTRAINT `user_attachment_ibfk_1` FOREIGN KEY (`id_discord`) REFERENCES `user_message` (`id_discord`);

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
