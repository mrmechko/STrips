git wget https://github.com/mrmechko/flaming-tyrion/archive/${data}.zip
unzip flaming-tyrion${data} -d flaming-tyrion
echo "strips.XMLSource = `pwd`/flaming-tyrion/lexicon/data/" > src/main/resources/application.conf
