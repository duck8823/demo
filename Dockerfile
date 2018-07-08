FROM maven

ADD . .

ENTRYPOINT ["mvn"]
CMD ["test"]
