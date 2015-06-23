git wget https://github.com/mrmechko/flaming-tyrion/archive/${data}.zip
unzip flaming-tyrion${data} -d flaming-tyrion
ls ./flaming-tyrion/lexicon
echo "strips.XMLSource = `pwd`/flaming-tyrion/lexicon/data/" > src/main/resources/application.conf
cat src/main/resources/application.conf
