-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 15-07-2019 a las 11:28:16
-- Versión del servidor: 10.1.40-MariaDB
-- Versión de PHP: 7.3.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `homeswitchhome`
--

--
-- Volcado de datos para la tabla `administradores`
--

INSERT INTO `administradores` (`mail`, `contraseña`, `id`) VALUES
('admin', '123', 1);

--
-- Volcado de datos para la tabla `propiedad`
--

INSERT INTO `propiedad` (`titulo`, `descripcion`, `pais`, `provincia`, `localidad`, `domicilio`, `monto`, `foto1`, `foto2`, `foto3`, `foto4`, `foto5`, `id`) VALUES
('prop_01', 'con reservas directa y hotsale + subasta', 'Argentina', 'Buenos Aires', 'Luján', '78/90', 111, NULL, NULL, NULL, NULL, NULL, 1),
('prop_02', 'con directa y hotsale disponible', 'Argentina', 'Buenos Aires', 'La Plata', '78/90', 222, NULL, NULL, NULL, NULL, NULL, 2),
('prop_03', 'con subasta activa (con ganador, cierra)', 'Argentina', 'Buenos Aires', 'La Plata', '78/90', 333, NULL, NULL, NULL, NULL, NULL, 3),
('prop_04', 'con futura subasta (abre, no cierra)', 'Argentina', 'Buenos Aires', 'Ensenada', '78/90', 333, NULL, NULL, NULL, NULL, NULL, 4),
('prop_05', 'con subasta activa (ofertas inválidas, con ganador, cierra)', 'Argentina', 'Buenos Aires', 'Ensenada', '78/90', 333, NULL, NULL, NULL, NULL, NULL, 5),
('prop_06', 'con subasta activa (sin ofertas, cierra)', 'Argentina', 'Buenos Aires', 'Ensenada', '78/90', 333, NULL, NULL, NULL, NULL, NULL, 6),
('prop_20', 'para verificar hotsales y abrir otro', 'Argentina', 'Buenos Aires', 'Berisso', '78/90', 333, NULL, NULL, NULL, NULL, NULL, 7);

--
-- Volcado de datos para la tabla `reservas`
--

INSERT INTO `reservas` (`propiedad`, `localidad`, `usuario`, `tipo`, `fecha_inicio`, `estado`, `monto`, `id`) VALUES
('prop_01', 'Luján', 'prem1@asd.com', 'directa', '2019-03-02', 'RESERVADA', 111, 1),
('prop_01', 'Luján', 'prem1@asd.com', 'hotsale', '2018-09-09', 'RESERVADA', 500, 2),
('prop_01', 'Luján', NULL, 'subasta', '2019-01-13', 'DISPONIBLE', 350, 3),
('prop_02', 'La Plata', NULL, 'directa', '2019-03-02', 'DISPONIBLE', 222, 4),
('prop_02', 'La Plata', NULL, 'hotsale', '2018-09-09', 'DISPONIBLE', 400, 5),
('prop_02', 'La Plata', NULL, 'directa', '2019-02-10', 'DISPONIBLE', 700, 6),
('prop_02', 'La Plata', NULL, 'directa', '2019-01-27', 'DISPONIBLE', 701, 7),
('prop_02', 'La Plata', 'com3@asd.com', 'directa', '2019-01-06', 'RESERVADA', 222, 8),
('prop_03', 'La Plata', NULL, 'subasta', '2019-01-06', 'DISPONIBLE', 100, 9),
('prop_04', 'Ensenada', NULL, 'directa', '2019-01-13', 'DISPONIBLE', 1000, 10),
('prop_05', 'Ensenada', NULL, 'subasta', '2019-01-06', 'DISPONIBLE', 50, 11),
('prop_06', 'Ensenada', NULL, 'subasta', '2019-01-06', 'DISPONIBLE', 3500, 12),
('prop_20', 'Berisso', NULL, 'hotsale', '2019-01-10', 'EN_ESPERA', 250, 13),
('prop_20', 'Berisso', NULL, 'hotsale', '2018-07-18', 'EN_ESPERA', 250, 14),
('prop_20', 'Berisso', NULL, 'directa', '2019-01-06', 'DISPONIBLE', 5, 15);

--
-- Volcado de datos para la tabla `solicitudes`
--

INSERT INTO `solicitudes` (`usuario`, `tipo`, `motivo`, `id`) VALUES
('prem2@asd.com', 'baja', 'Ya no quiero pagar la cuota', 1),
('com2@asd.com', 'alta', 'Quiero poder realizar reservas directas', 2);

--
-- Volcado de datos para la tabla `subastas`
--

INSERT INTO `subastas` (`propiedad`, `localidad`, `fecha_inicio`, `fecha_subasta`, `montos`, `usuarios`, `id`) VALUES
('prop_03', 'La Plata', '2019-01-06', '2019-07-07', NULL, NULL, 1),
('prop_05', 'Ensenada', '2019-01-06', '2019-07-08', '700 600 600', 'com2@asd.com com3@asd.com prem3@asd.com', 2),
('prop_06', 'Ensenada', '2019-01-06', '2019-07-09', NULL, NULL, 3),
('prop_01', 'Luján', '2019-01-13', '2019-07-14', '350', 'prem3@asd.com', 4);

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`mail`, `contraseña`, `nombre`, `apellido`, `f_nac`, `creditos`, `nro_tarj`, `marca_tarj`, `titu_tarj`, `venc_tarj`, `cod_tarj`, `premium`, `id`) VALUES
('prem1@asd.com', '123', 'Pedro', 'Pérez', '1990-01-01', 15, 888888888, 'VISA', 'PEDRO PEREZ', '2020-05-01', 1234, 1, 1),
('prem2@asd.com', '123', 'Juan', 'Garcia', '1985-02-02', 1, 888888888, 'MasterCard', 'JUAN GARCIA', '2020-05-01', 1234, 1, 2),
('com1@asd.com', '123', 'Carlos', 'Rodriguez', '1987-03-03', 1, 888888888, 'VISA', 'CARELOS RODRIG', '2020-05-01', 1234, 0, 3),
('com2@asd.com', '123', 'Javier', 'Hernandez', '1988-06-01', 0, 888888888, 'VISA', 'JAVIER H', '2020-05-01', 1234, 0, 4),
('com3@asd.com', '123', 'Lucas', 'Fernandez', '1999-05-05', 2, 888888888, 'VISA', 'LUCAS F', '2020-05-01', 1234, 0, 5),
('prem3@asd.com', '123', 'Santiago', 'Romero', '1970-06-06', 1, 888888888, 'MasterCard', 'SANTIAGO R', '2020-05-01', 1234, 1, 6);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
