# KerberosConKafka
Este proyecto es  un Api con acceso a kafka securizado con Kerberos.


Este proyecto es KAFKA es un Api que expone una operación de escritura a una cola kafka securizado con Kerberos, totalmente limpia.
La configuración va en un properties.
Para acceso a kafka necesita tener dentro del contenedor el fichero krb5.conf , el fihcero hdp_b2bp_app.keytab. 
Tambien van los proyectos donde se ha aplicado y que los accesos a kafka los tienen dentro, no usa el api KAFKA, pero esta bien diferenciado como se usa.
Tambien dentro de los proyectos tracking podemos ver el acceso a HBASE.
