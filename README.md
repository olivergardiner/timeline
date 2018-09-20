# timeline
Creates timeline slides in a presentation format (currently OOXML slides) based on a project plan in MS Project XML format

The code uses a DOM parser to create a prettified diagram from a Microsoft Project XML file. The choice of DOM (I initially tried JAXB from the official schemas) is because there are a number of free Project tools that export in a flavour of the official format but they tend not to parse against the exact schema. Using DOM means that tool is tolerant to such vaguaries and it is easy enough to get to the key elements.
