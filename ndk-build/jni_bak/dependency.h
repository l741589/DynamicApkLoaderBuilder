#pragma once
#include <stdio.h> 
#include <stdlib.h> 
#include <stdarg.h>
#include <string>
#include <functional>
#include <dlfcn.h>
#include <android/log.h>
#include <vector>
#include <algorithm>
namespace cocos2d {
	const char*cocos2dVersion();
	struct CCZHeader {
		unsigned char   sig[4];             /** Signature. Should be 'CCZ!' 4 bytes. */
		unsigned short  compression_type;   /** Should be 0. */
		unsigned short  version;            /** Should be 2 (although version type==1 is also supported). */
		unsigned int    reserved;           /** Reserved for users. */
		unsigned int    len;                /** Size of the uncompressed file. */
	};
	class FileUtils {
	public:
		
		static FileUtils*getInstance();
		const std::vector<std::string>& getSearchPaths() const;
		virtual std::string getStringFromFile(const std::string& filename);
	};

	class ZipUtils {
	public:
		static unsigned int s_uEncryptedPvrKeyParts[4];
		static bool isCCZFile(const char *filename);
		static int inflateCCZFile(const char *filename, unsigned char **out);
		static void decodeEncodedPvr(unsigned int *data, ssize_t len);
	};
};