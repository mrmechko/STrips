wget https://github.com/mrmechko/flaming-tyrion/archive/${data}.zip
ls
unzip ${data} -d flaming-tyrion
ls 
echo "strips.XMLSource = `pwd`/flaming-tyrion/lexicon/data/" > src/main/resources/application.conf
cat src/main/resources/application.conf
