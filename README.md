# Spring-Batch--Read-CSV-and-Excel-files
Following functinalities are included in the repository:
1. Read csv and excel files from a folder at regular intervals using spring batch framework. 
2. Data is read and converted to JSON with key as headers and value as values from the file. Custom item readers are used to transform the read data into a map.
3. Store the read data into H2 database.
4. Reading of multiple files and multiples lines in a file in parllel. This provides faster processing of files but order in which data is read is not maintained.
5. Uses chunk processing concept. Chunk size is configurable.
6. Listeners are added to control and alter the proccessing the files.
7. REST APIs to retrieve information from the database.
8. Basic validations for the read data from the files.
