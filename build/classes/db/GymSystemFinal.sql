-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 17, 2024 at 07:48 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `comp_sys`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_sub`
--

CREATE TABLE `tbl_sub` (
  `s_id` int(50) NOT NULL,
  `u_id` int(50) NOT NULL,
  `s_fname` varchar(50) NOT NULL,
  `s_lname` varchar(50) NOT NULL,
  `s_sdate` date NOT NULL,
  `s_edate` date NOT NULL,
  `s_subtype` varchar(50) NOT NULL,
  `s_status` varchar(50) NOT NULL,
  `s_image` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_sub`
--

INSERT INTO `tbl_sub` (`s_id`, `u_id`, `s_fname`, `s_lname`, `s_sdate`, `s_edate`, `s_subtype`, `s_status`, `s_image`) VALUES
(113, 20, 'asdasdsds', 'sssddsds', '2024-06-17', '2025-06-17', 'Yearly', 'Active', ''),
(118, 20, 'asfafsasdsd', 'asfasfdsad', '2024-06-17', '2024-06-14', 'Daily', 'Active', ''),
(120, 20, 'afdfsdg', 'gdsgsdg', '2024-06-18', '2024-07-18', 'Monthly', 'Pending', '');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_user`
--

CREATE TABLE `tbl_user` (
  `user_id` int(50) NOT NULL,
  `user_firstname` varchar(50) NOT NULL,
  `user_lastname` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `user_email` varchar(50) NOT NULL,
  `user_account` varchar(50) NOT NULL,
  `user_status` varchar(50) NOT NULL,
  `user_image` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_user`
--

INSERT INTO `tbl_user` (`user_id`, `user_firstname`, `user_lastname`, `username`, `password`, `user_email`, `user_account`, `user_status`, `user_image`) VALUES
(18, 'vincent', 'test', 'ant', '0123456789', 'test1@gmail.com', 'Admin', 'Active', ''),
(19, 'vincent', 'test', 'test', '1234567890', 'al@gmail.com', 'User', 'Active', ''),
(20, 'danz', 'danzer', 'dan234', 'x3Xnt1ft5jDNCqERO9ECZhqziCnKUqZCKreChi8mhkY=', 'dan@gmail.com', 'Manager', 'Active', ''),
(21, 'ad', 'ad', 'ad', 'x3Xnt1ft5jDNCqERO9ECZhqziCnKUqZCKreChi8mhkY=', 'ad@gmail.com', 'User', 'Active', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_sub`
--
ALTER TABLE `tbl_sub`
  ADD PRIMARY KEY (`s_id`),
  ADD KEY `u_id` (`u_id`);

--
-- Indexes for table `tbl_user`
--
ALTER TABLE `tbl_user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_sub`
--
ALTER TABLE `tbl_sub`
  MODIFY `s_id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=121;

--
-- AUTO_INCREMENT for table `tbl_user`
--
ALTER TABLE `tbl_user`
  MODIFY `user_id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tbl_sub`
--
ALTER TABLE `tbl_sub`
  ADD CONSTRAINT `sub_tbl` FOREIGN KEY (`u_id`) REFERENCES `tbl_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
