wget https://github.com/mrmechko/flaming-tyrion/archive/v${data}.zip
ls
unzip -q v${data}
ls 
echo "strips.XMLSource = `pwd`/flaming-tyrion-${data}/lexicon/data/" > src/main/resources/application.conf
ls `pwd`/flaming-tyrion-${data}/lexicon/data | wc -l
cat src/main/resources/application.conf
