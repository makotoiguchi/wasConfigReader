# Short Description

Reads WebSphere Application Server (WAS v 6.x, 7.x, 8.x) xml configuration files and outputs an .txt file.
Superficially compares two was configurations.


# Running Configurations

* Target class: Reader
  * Arguments: 

  `<cell config folder>`

  `"C:\Projects\WebSphere MPA\original was"`
  
  * Arguments: 

  `<cell config folder> <output text file>`

  `"C:\Projects\WebSphere MPA\original was" "C:\Projects\WebSphere MPA\original was\originalWas-output.txt"`

* Target class: Compare
  * Arguments:

  `<source cell config folder> <destination cell config folder> <output folder for text files>`

  `"C:\Projects\WebSphere MPA\original was" "C:\Projects\WebSphere MPA\new was" "C:\Projects\WebSphere MPA\comparison"`

  * Output files:

  ```
  config-<source's folder name>.txt
  config-<destination's folder name>.txt
  diff-<source's folder name>.txt
  diff-<destination's folder name>.txt
  common-<source's folder name>-<destination's folder name>.txt
  ```

  ```
  config-original was.txt
  config-new was.txt
  diff-original was.txt
  diff-new was.txt
  common-original was-new was.txt
  ```


# Expected XML Configuration Tree Structure
```
<Cell config folder>/
|   cell.xml
|   security.xml
|   virtualhosts.xml
|   libraries.xml
|   resources.xml
|   variables.xml
|   
+---clusters
|   +---<Cluster config folder>/
|           cluster.xml
|           libraries.xml
|           resources.xml
|           variables.xml
|           
+---nodes
    +---<Node config folder>/
        |   node.xml
        |   libraries.xml
        |   resources.xml
        |   variables.xml
        |   systemapps.xml
        |   serverindex.xml
        |
        +---servers
            +---<Server/JVM config folder>/
                    server.xml
                    resources.xml
                    variables.xml
```

*Example Cell config folder:*
   *    */opt/IBM/WebSphere/AppServer/profiles/<Profile>/config/cells/<Cell config folder>*
