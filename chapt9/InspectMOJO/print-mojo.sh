# reference 
java -cp h2o-genmodel.jar hex.genmodel.tools.PrintMojo  -i "best2_mojo.zip" -o tree.png --format png --tree 0
java -cp h2o-genmodel.jar hex.genmodel.tools.PrintMojo  -i "best2_mojo.zip" -o output/tree.json --format json --tree 0
# creates a .dot file that can be rendered by Graphviz
java -cp h2o-genmodel.jar hex.genmodel.tools.PrintMojo  -i "best2_mojo.zip" -o output/tree.dot --format dot --tree 0