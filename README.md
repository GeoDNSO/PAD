# PAD
Repositorio para la creación y gestión de las prácticas y el proyecto de PAD(Programación de Aplicaciones para Dispositivos Móviles).

En **Proyecto** se encuentra la aplicación Android para el proyecto de PAD.
En **Proyecto Backend** se encuentra el código necesario para encender el servidor que actua como API para la aplicación.

## Construir el servidor
Para ejecutar el servidor es necesario ejecutar los siguientes comandos (es necesario tener instalada una versión actualizada de Python):
* Instalar las dependencias
```
pip install -r requirements.txt
```
* Ejecutar el servidor
```
python app.py
```
## Ejecutar la aplicación Android
Para ejecutar la aplicación Android bastaría con sincronizar la aplicación con el gradle y ejecutarla en un emulador o dispositivo móvil.
