# PAD
Repositorio para la creación y gestión de las prácticas y el proyecto de PAD(Programación de Aplicaciones para Dispositivos Móviles).

* En **Proyecto** se encuentra la aplicación Android para el proyecto de PAD.
* En **Proyecto Backend** se encuentra el código necesario para encender el servidor que actua como API para la aplicación.

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

Shield: [![CC BY-NC-SA 4.0][cc-by-nc-sa-shield]][cc-by-nc-sa]

This work is licensed under a
[Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License][cc-by-nc-sa].

[![CC BY-NC-SA 4.0][cc-by-nc-sa-image]][cc-by-nc-sa]

[cc-by-nc-sa]: http://creativecommons.org/licenses/by-nc-sa/4.0/
[cc-by-nc-sa-image]: https://licensebuttons.net/l/by-nc-sa/4.0/88x31.png
[cc-by-nc-sa-shield]: https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0-lightgrey.svg
