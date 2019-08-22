chisel:
	docker run --rm -v ${shell pwd}:/build amethyst-chisel /bin/sh -c "cd /build && sbt run"

docker:
	 docker build -t amethyst-chisel:latest .

.PHONY: clean
clean :
	sudo rm *.v *.fir *.anno
