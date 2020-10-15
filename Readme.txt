Notas de Instalación / Prueba
Octavio Martínez García


• Para la instalación de la aplicación, basta con que el directorio de Imágenes (que contiene las imágenes básicas usadas por la aplicación) esté en la misma ruta que el ejecutable.

• En el directorio del Ejecutable hay una carpeta de imágenes básicas usadas por la aplicación (comandos, botones, etc.) que debe encontrarse en el mismo directorio que el ejecutable en .jar, ya que las rutas se han establecido de forma relativa para hacer referencia a esta carpeta de imágenes. 

• Asímismo se ha incluido un ejecutable nativo de windows creado a partir del .jar con JSmooth, el cual debe estar en el mismo directorio que el .jar, ya que obtiene el classpath y la clase principal de forma relativa a este directorio.

• Para importar el proyecto en Eclipse con fines de ejecución y prueba basta con cargar la carpeta Arquitectura/PFG dentro del entorno de desarrollo. Dentro de este directorio se encuentra toda la arquitectura de la aplicación utilizada para el desarrollo, incluyendo el código fuente, las imágenes necesarias, la documentación del proyecto, la API de la versión actual (0.11) y los diversos diagramas UML.

• En las diversas carpetas dentro del directorio «Documentación PFG» de la Arquitectura, que hacen referencia a las áreas del UP, se han eliminado los documentos en LyX creados durante el desarrollo (Modelo de Casos de Uso, Especificación Complementaria, Modelo de Diseño, etc.) para documentar el trabajo. Esto es debido a que la mayoría de esos documentos individuales se han agregado para conformar la presente memoria.