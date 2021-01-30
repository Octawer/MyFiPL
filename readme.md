# MyFiPL

<div>
El proyecto se trata de desarrollar una aplicación educativa que permita a alumnos de primaria familiarizarse con la programación y desarrollar sus primeros programas.
Se trata de un entorno gráfico, donde los alumnos podrán utilizar sentencias del lenguaje de programación gráfico diseñado, consistentes en imágenes cargadas en la aplicación,
para construir programas que controlen las acciones de un robot, representado por otra imagen o pequeña animación, 
en un escenario, el cual vendrá dado por una imagen de fondo cargada desde un archivo.
</div>

<div>
El sistema será un entorno gráfico en el cual se podrá programar el comportamiento de uno o varios robots que se mueven por un escenario e interactúan con el. 
Tanto el escenario como el robot serán imágenes que se podrán cargar en el entorno, pudiendo variarse ambos para dar a los alumnos la posibilidad de crear diferentes programas.
</div>

<div>
El entorno gráfico estará compuesto por varias áreas de trabajo, entre las cuales están:
</div>

<div>
<ul>
<li>Panel de programación del robot: área donde se podrán programar las acciones que realizará el robot en el escenario, el cual se divide en</li>
<li>
    <ul>
        <li>Paleta de comandos: panel donde se mostrarán las todas las acciones y comandos aplicables al robot</li>
        <li>Escritorio: panel donde se colocarán los comandos para crear el programa que debe ejecutar el robot</li>
    </ul>
</li>
<li>Panel de Control: permitirá el control de la ejecución de los programas presentes en el escritorio</li>
<li>Escenario: panel donde se mostrará la simulación de las acciones de los robots sobre el escenario</li>
</ul>
</div>


<b>Notas de Instalacion / Prueba</b>

<div>
Para la instalación de la aplicación, basta con descomprimir el archivo en cualquier directorio, siempre que el directorio de Imágenes (que contiene las imágenes básicas usadas por la aplicación) esté en la misma ruta que el ejecutable.

En el directorio del Ejecutable hay una carpeta de imágenes básicas usadas por la aplicación (comandos, botones, etc.) que debe encontrarse en el mismo directorio que el ejecutable en .jar, ya que las rutas se han establecido de forma relativa para hacer referencia a esta carpeta de imágenes.

Asímismo se ha incluido un ejecutable nativo de windows creado a partir del .jar con JSmooth, el cual debe estar en el mismo directorio que el .jar, ya que obtiene el classpath y la clase principal de forma relativa a este directorio.

Para importar el proyecto en Eclipse con fines de ejecución y prueba basta con cargar la carpeta Arquitectura/PFG dentro del entorno de desarrollo. Dentro de este directorio se encuentra toda la arquitectura de la aplicación utilizada para el desarrollo, incluyendo el código fuente, las imágenes necesarias, la documentación del proyecto, la API de la versión actual (0.11) y los diversos diagramas UML.

En las diversas carpetas dentro del directorio «Documentación PFG» de la Arquitectura, que hacen referencia a las áreas del UP, se han eliminado los documentos en LYX creados durante el desarrollo (Modelo de Casos de Uso, Especificación Complementaria, Modelo de Diseño, etc.) para documentar el trabajo. 
Esto es debido a que la mayoría de esos documentos individuales se han agregado para conformar la presente memoria.
</div>

Documentación
-------

<div><a href="https://github.com/Octawer/MyFiPL/tree/master/doc">Documentacion</a></div>
<div><a href="https://github.com/Octawer/MyFiPL/tree/master/doc/10%20Memoria">Memoria</a></div>
<div><a href="https://github.com/Octawer/MyFiPL/wiki">Wiki</a></div>

Screens
-------

<div>
<img src="doc\11 Screenshots\PFG_VideoEjemplo_nv_4A_Moment.jpg" />
<img src="doc\11 Screenshots\PFG_VideoEjemplo_nv_4A_Moment_2.jpg" />
<img src="doc\11 Screenshots\PFG_VideoEjemplo_nv_4A_Moment_3.jpg" />
</div>


Author
-------

Octavio Martinez