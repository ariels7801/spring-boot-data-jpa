/* Populate clientes */
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Andres', 'Guzman', 'profesor@bolsadeideas.com', '2017-08-28','');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('John', 'Doe', 'jothn@gmail.com', '2017-08-28','');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES('Edit', 'Hernandez', 'edit@gmail.com', '2017-08-28','');

/* Populate productos */
INSERT INTO productos (nombre, precio, create_at) VALUES('Panasonic Pantalla LCD', 7850,  NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Sony Camara digital DSC-W320B', 1230,  NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Apple iPod shuffle', 4510,  NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES('Sony Notebook Z110', 9860,  NOW());

/* Populate facturas */
INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES('Factura equipos de oficina',  null, 1, NOW());
INSERT INTO facturas_items(cantidad, factura_id, producto_id) VALUES(1, 1, 1);
INSERT INTO facturas_items(cantidad, factura_id, producto_id) VALUES(2, 1, 4);
INSERT INTO facturas_items(cantidad, factura_id, producto_id) VALUES(1, 1, 5);