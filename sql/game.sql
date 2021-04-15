-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: game
-- ------------------------------------------------------
-- Server version	5.7.11-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `game_player`
--

DROP TABLE IF EXISTS `game_player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_player` (
  `player_index` int(11) unsigned NOT NULL COMMENT '玩家id',
  `name` varchar(45) DEFAULT NULL COMMENT '玩家昵称',
  `register_time` int(11) DEFAULT NULL COMMENT '注册时间',
  `login_ip` varchar(45) DEFAULT NULL COMMENT '登陆ip',
  `last_login_time` int(11) DEFAULT NULL COMMENT '最后一次登陆时间',
  `header` varchar(45) DEFAULT NULL COMMENT '玩家头像',
  `server_id` smallint(4) DEFAULT '0',
  `open_id` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`player_index`),
  UNIQUE KEY `player_index_UNIQUE` (`player_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_player`
--

LOCK TABLES `game_player` WRITE;
/*!40000 ALTER TABLE `game_player` DISABLE KEYS */;
/*!40000 ALTER TABLE `game_player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_player_item`
--

DROP TABLE IF EXISTS `game_player_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_player_item` (
  `id` int(11) unsigned NOT NULL,
  `player_index` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `item_count` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_player_item`
--

LOCK TABLES `game_player_item` WRITE;
/*!40000 ALTER TABLE `game_player_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `game_player_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_player_role`
--

DROP TABLE IF EXISTS `game_player_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_player_role` (
  `player_index` int(11) NOT NULL,
  `job` smallint(4) DEFAULT NULL,
  `sex` tinyint(2) DEFAULT NULL,
  PRIMARY KEY (`player_index`),
  UNIQUE KEY `player_index_UNIQUE` (`player_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_player_role`
--

LOCK TABLES `game_player_role` WRITE;
/*!40000 ALTER TABLE `game_player_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `game_player_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_player_scene`
--

DROP TABLE IF EXISTS `game_player_scene`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_player_scene` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `player_index` int(11) NOT NULL,
  `player_position_x` float(5,2) NOT NULL DEFAULT '0.00',
  `player_position_y` float(5,2) NOT NULL DEFAULT '0.00',
  `scene_id` tinyint(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_player_scene`
--

LOCK TABLES `game_player_scene` WRITE;
/*!40000 ALTER TABLE `game_player_scene` DISABLE KEYS */;
/*!40000 ALTER TABLE `game_player_scene` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-16  0:57:06
